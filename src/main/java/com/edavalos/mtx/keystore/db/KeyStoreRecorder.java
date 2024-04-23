package com.edavalos.mtx.keystore.db;

import com.edavalos.mtx.keystore.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public final class KeyStoreRecorder {
    private KeyStoreRecorder() { }

    public static void recordKeyValue(String appId, String key, String value, String timestamp) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Util.connectToDb(
                    DbConst.POSTGRES_DRIVER_CLASSNAME,
                    DbConst.POSTGRES_URL + DbConst.POSTGRES_DB_NAME,
                    DbConst.POSTGRES_DB_USER,
                    DbConst.POSTGRES_DB_PASS
            );

            QueryInsertKvBuilder query = new QueryInsertKvBuilder(
                    connection,
                    appId,
                    key,
                    value,
                    timestamp
            );
            statement = query.getPreparedStatement();
            statement.executeUpdate();
            connection.commit();

            System.out.println("KV pair stored in database");
        } catch (SQLException e) {
            System.err.println("Failed to run sql query: " + e);
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
