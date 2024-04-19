package com.edavalos.mtx.keystore.db;

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
        return null;
    }
}
