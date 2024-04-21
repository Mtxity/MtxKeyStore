package com.edavalos.mtx.keystore.db;

public final class DbConst {
    private DbConst() { }

    static final String SCHEMA_NAME = "MtxKvStore";
    static final String TABLE_KV_NAME = "kv";
    static final String TABLE_APP_NAME = "app";

    static final String POSTGRES_DRIVER_CLASSNAME = "org.postgresql.Driver";

    static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/";
    static final String POSTGRES_DB_NAME = "mtxkvstore";
    static final String POSTGRES_DB_USER = "mtxkvstore";
    static final String POSTGRES_DB_PASS = "mtxkvstore";
}
