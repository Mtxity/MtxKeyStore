package com.edavalos.mtx.keystore.rcon;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RconClientTest {
    @Test
    void packetBytesUsesSourceRconLittleEndianLayout() {
        byte[] packet = RconClient.packetBytes(7, 2, "say hi");

        assertEquals(20, packet.length);
        assertEquals(16, packet[0]);
        assertEquals(0, packet[1]);
        assertEquals(7, packet[4]);
        assertEquals(2, packet[8]);
        assertEquals("say hi", new String(packet, 12, 6, StandardCharsets.UTF_8));
        assertEquals(0, packet[packet.length - 1]);
    }

    @Test
    void packetBytesSupportsUtf8Commands() {
        byte[] packet = RconClient.packetBytes(1, 3, "say café");

        assertTrue(new String(packet, StandardCharsets.ISO_8859_1).contains("cafÃ©"));
    }
}
