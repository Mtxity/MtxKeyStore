package com.edavalos.mtx.keystore.db;

import java.sql.Connection;
import java.util.List;

public class QueryInsertKvBuilder extends QueryBuilder {
    private final List<KvRow> rows;

    public QueryInsertKvBuilder(Connection connection, String dbTable, List<KvRow> rows) {
        super(connection, dbTable);
        this.rows = rows;
    }

    public QueryInsertKvBuilder(Connection connection, String dbTable, KvRow row) {
        super(connection, dbTable);
        this.rows = List.of(row);
    }

    public QueryInsertKvBuilder(Connection connection,
                                String dbTable,
                                String appId,
                                String key,
                                String value,
                                String timestamp) {
        this(connection, dbTable, new KvRow(appId, key, value, timestamp));
    }
}
