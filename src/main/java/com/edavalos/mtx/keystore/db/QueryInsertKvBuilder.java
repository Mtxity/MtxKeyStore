package com.edavalos.mtx.keystore.db;

import com.edavalos.mtx.keystore.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QueryInsertKvBuilder extends QueryBuilder {
    private final List<KvRow> rows;

    public QueryInsertKvBuilder(Connection connection, List<KvRow> rows) {
        super(connection, DbConst.TABLE_APP_NAME);
        this.rows = rows;
    }

    public QueryInsertKvBuilder(Connection connection, KvRow row) {
        super(connection, DbConst.TABLE_APP_NAME);
        this.rows = List.of(row);
    }

    public QueryInsertKvBuilder(Connection connection,
                                String appId,
                                String key,
                                String value,
                                String timestamp) {
        this(connection, new KvRow(appId, key, value, timestamp));
    }

    protected String getQuery() {
        String[] values = new String[this.rows.size()];
        int i = 0;
        for (KvRow row : this.rows) {
            values[i++] = "('" + Util.getConcat("', '", row.appId(), row.key(), row.val(), row.timestamp()) + "')";
        }

        return "INSERT INTO \"" + super.dbName + "\"." + super.table + " (" +
                "   app_id, key, val, lastSetTimestamp " +
                ") VALUES " +
                Util.getConcat(", ", values) + ";";
    }
}
