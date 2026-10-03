package com.edavalos.mtx.keystore.rcon;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/** Minimal client for the Source RCON protocol used by Minecraft and other servers. */
public class RconClient {
    private static final int SERVERDATA_AUTH = 3;
    private static final int SERVERDATA_EXECCOMMAND = 2;
    private static final int SERVERDATA_AUTH_RESPONSE = 2;

    private final String host;
    private final int port;
    private final String password;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    public RconClient(String host, int port, String password,
                      int connectTimeoutMillis, int readTimeoutMillis) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public String send(String command) throws RconException {
        if (command == null || command.isBlank()) {
            throw new RconException("RCON command must not be blank");
        }
        if (password == null || password.isEmpty()) {
            throw new RconException("RCON password is not configured");
        }

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), connectTimeoutMillis);
            socket.setSoTimeout(readTimeoutMillis);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            writePacket(output, 1, SERVERDATA_AUTH, password);
            Packet authResponse = readPacket(input);
            if (authResponse.id() == -1 || authResponse.type() != SERVERDATA_AUTH_RESPONSE) {
                throw new RconException("RCON authentication failed");
            }

            writePacket(output, 2, SERVERDATA_EXECCOMMAND, command);
            StringBuilder response = new StringBuilder();
            Packet packet = readPacket(input);
            response.append(packet.body());

            // Large responses can be split into multiple packets. A short timeout marks
            // the end of optional packets without delaying normal one-packet responses.
            socket.setSoTimeout(Math.min(readTimeoutMillis, 100));
            while (true) {
                try {
                    response.append(readPacket(input).body());
                } catch (IOException ignored) {
                    break;
                }
            }
            return response.toString();
        } catch (IOException e) {
            throw new RconException("Unable to send RCON command to " + host + ":" + port, e);
        }
    }

    static byte[] packetBytes(int id, int type, String body) {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream packet = new ByteArrayOutputStream();
        writeLittleEndianInt(packet, 10 + bodyBytes.length);
        writeLittleEndianInt(packet, id);
        writeLittleEndianInt(packet, type);
        packet.writeBytes(bodyBytes);
        packet.write(0);
        packet.write(0);
        return packet.toByteArray();
    }

    private static void writePacket(DataOutputStream output, int id, int type, String body)
            throws IOException {
        output.write(packetBytes(id, type, body));
        output.flush();
    }

    private static Packet readPacket(DataInputStream input) throws IOException, RconException {
        int size = readLittleEndianInt(input);
        if (size < 10 || size > 4 * 1024 * 1024) {
            throw new RconException("Invalid RCON packet size: " + size);
        }
        byte[] payload = input.readNBytes(size);
        if (payload.length != size) {
            throw new EOFException("Incomplete RCON packet");
        }
        int id = littleEndianInt(payload, 0);
        int type = littleEndianInt(payload, 4);
        String body = new String(payload, 8, size - 10, StandardCharsets.UTF_8);
        return new Packet(id, type, body);
    }

    private static int readLittleEndianInt(DataInputStream input) throws IOException {
        return Integer.reverseBytes(input.readInt());
    }

    private static int littleEndianInt(byte[] bytes, int offset) {
        return (bytes[offset] & 0xff)
                | ((bytes[offset + 1] & 0xff) << 8)
                | ((bytes[offset + 2] & 0xff) << 16)
                | (bytes[offset + 3] << 24);
    }

    private static void writeLittleEndianInt(ByteArrayOutputStream output, int value) {
        output.write(value & 0xff);
        output.write((value >>> 8) & 0xff);
        output.write((value >>> 16) & 0xff);
        output.write((value >>> 24) & 0xff);
    }

    private record Packet(int id, int type, String body) { }
}
