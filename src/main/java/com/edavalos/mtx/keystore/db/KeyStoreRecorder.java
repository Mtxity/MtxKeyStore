package com.edavalos.mtx.keystore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class KeyStoreRecorder {
    private KeyStoreRecorder() { }

    public static void recordKeyValue(String appId, String key, String value) {
        Connection connection = null;
        Statement statement = null;

        try {
            String query = "";
            // Ensuring driver class exists / preloading it
            Class.forName("org.postgresql.Driver");

            connection =  DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/mtxkvstore",
                    "mtxkvstore",
                    "mtxkvstore"
            );
            statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
//            if (connection != null) {
//                connection
//            }
        }
    }
}
