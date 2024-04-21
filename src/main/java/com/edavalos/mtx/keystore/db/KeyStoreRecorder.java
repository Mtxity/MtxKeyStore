package com.edavalos.mtx.keystore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public final class KeyStoreRecorder {
    private KeyStoreRecorder() { }

    public static void recordKeyValue(String appId, String key, String value, String timestamp) {
        Connection connection = null;
        Statement statement = null;
        String query = "INSERT INTO \"" + DbConst.SCHEMA_NAME + "\"." + DbConst.TABLE_APP_NAME + " (" +
                "   app_id, key, val, lastSetTimestamp" +
                ") VALUES (" +
                "   " + appId + ", " + key + ", " + value + ", " + timestamp +
                ");";

        try {
            // Ensuring driver class exists / preloading it
            Class.forName(DbConst.POSTGRES_DRIVER_CLASSNAME);

            connection =  DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/mtxkvstore",
                    "mtxkvstore",
                    "mtxkvstore"
            );

            statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.commit();

            System.out.println("KV pair stored in database");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e);
            }
        }
    }
}
