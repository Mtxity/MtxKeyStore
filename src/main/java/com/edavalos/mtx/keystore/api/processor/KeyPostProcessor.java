package com.edavalos.mtx.keystore.api.processor;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.model.KeyValueInfo;

import java.util.HashMap;

import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStore;
import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStoreTimestamps;

public final class KeyPostProcessor {
    private KeyPostProcessor() { }

    public static KeyValueInfo storeValue(String appId, String key, String val) {
        if (!mainKeyStore.containsKey(appId)) {
            mainKeyStore.put(appId, new HashMap<>());
        }
        HashMap<String, String> kv = mainKeyStore.get(appId);

        if (!mainKeyStoreTimestamps.containsKey(appId)) {
            mainKeyStoreTimestamps.put(appId, new HashMap<>());
        }

        String previousVal = null;
        if (kv.containsKey(key)) {
            previousVal = kv.get(key);
        }

        kv.put(key, val);
        mainKeyStoreTimestamps.get(appId).put(key, Util.getTimestamp());

        return new KeyValueInfo(appId, key, previousVal, val);
    }
}
