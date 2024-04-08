package com.edavalos.mtx.keystore.api.processor;

import com.edavalos.mtx.keystore.api.model.ValueInfo;

import java.util.HashMap;

import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStore;
import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStoreTimestamps;

public final class KeyGetProcessor {
    private KeyGetProcessor() { }

    public static ValueInfo getValue(String appId, String key) {
        if (!mainKeyStore.containsKey(appId)) {
            mainKeyStore.put(appId, new HashMap<>());
        }
        HashMap<String, String> kv = mainKeyStore.get(appId);

        if (!kv.containsKey(key)) {
            return null;
        }

        String lastModifiedTimestamp = null;
        if (mainKeyStoreTimestamps.containsKey(appId)) {
            if (mainKeyStoreTimestamps.get(appId).containsKey(key)) {
                lastModifiedTimestamp = mainKeyStoreTimestamps.get(appId).get(key);
            }
        }

        return new ValueInfo(appId, key, kv.get(key), lastModifiedTimestamp);
    }
}
