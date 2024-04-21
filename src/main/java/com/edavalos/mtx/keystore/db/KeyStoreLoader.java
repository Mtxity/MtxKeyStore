package com.edavalos.mtx.keystore.db;

import com.edavalos.mtx.keystore.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class KeyStoreLoader {
    private KeyStoreLoader() { }

    record KvRow(
            String appId,
            String key,
            String val,
            String timestamp
    ) { }

    public static List<KvRow> loadKeyValues() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet;
        String query = "SELECT * FROM \"" + DbConst.SCHEMA_NAME + "\"." + DbConst.TABLE_APP_NAME + ";";
        List<KvRow> results = new ArrayList<>();

        try {
            connection = Util.connectToDb(
                    DbConst.POSTGRES_DRIVER_CLASSNAME,
                    DbConst.POSTGRES_URL + DbConst.POSTGRES_DB_NAME,
                    DbConst.POSTGRES_DB_USER,
                    DbConst.POSTGRES_DB_PASS
            );

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String appId = resultSet.getString("app_id");
                String key = resultSet.getString("key");
                String val = resultSet.getString("val");
                String timestamp = resultSet.getString("lastSetTimestamp");
                results.add(new KvRow(appId, key, val, timestamp));
            }

            System.out.println("Successfully queried and loaded all KV pairs");
        } catch (SQLException e) {
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
        return results;
    }
}
