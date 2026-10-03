package com.edavalos.mtx.keystore.db;

import com.edavalos.mtx.keystore.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QueryInsertKvBuilder extends QueryBuilder {
    private final List<KvRow> rows;

    public QueryInsertKvBuilder(Connection connection, List<KvRow> rows) {
        super(connection, DbConst.TABLE_KV_NAME);
        this.rows = rows;
    }

    public QueryInsertKvBuilder(Connection connection, KvRow row) {
        super(connection, DbConst.TABLE_KV_NAME);
        this.rows = List.of(row);
    }

    public QueryInsertKvBuilder(Connection connection,
                                String appId,
                                String key,
                                String value,
                                String timestamp) {
        this(connection, new KvRow(appId, key, value, timestamp));
    }

    @Override
    protected String getQuery() {
        if (this.rows == null || this.rows.isEmpty()) {
            return "";
        }

        String[] values = new String[this.rows.size()];
        for (int i = 0; i < this.rows.size(); i++) {
            values[i] = "(?, ?, ?, ?)";
        }

        return "INSERT INTO \"" + super.dbName + "\"." + super.table + " (" +
                "   app_id, key, val, lastSetTimestamp " +
                ") VALUES " +
                Util.getConcat(", ", values) +
                " ON CONFLICT (app_id, key)" +
                " DO UPDATE SET" +
                "   val = EXCLUDED.val," +
                "   lastSetTimestamp = EXCLUDED.lastSetTimestamp;";
    }

    @Override
    protected void setParameters(PreparedStatement statement) throws SQLException {
        int parameterIndex = 1;
        for (KvRow row : this.rows) {
            statement.setString(parameterIndex++, row.appId());
            statement.setString(parameterIndex++, row.key());
            statement.setString(parameterIndex++, row.val());
            statement.setString(parameterIndex++, row.timestamp());
        }
    }
}
