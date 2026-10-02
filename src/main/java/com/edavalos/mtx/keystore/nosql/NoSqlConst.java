package com.edavalos.mtx.keystore.nosql;

public final class NoSqlConst {
    private NoSqlConst() { }

    static final String KEY_PREFIX = "mtxkvstore:app:";
    static final String APP_IDS_KEY = "mtxkvstore:apps";

    static String getAppKey(String appId) {
        return KEY_PREFIX + appId;
    }
}
