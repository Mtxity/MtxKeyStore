package com.edavalos.mtx.keystore.api.processor;

import com.edavalos.mtx.keystore.api.model.KeyValueInfo;

import java.util.HashMap;

import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStore;

public final class KeyPostProcessor {
    private KeyPostProcessor() { }

    public static KeyValueInfo storeValue(String appId, String key, String val) {
        if (!mainKeyStore.containsKey(appId)) {
            mainKeyStore.put(appId, new HashMap<>());
        }
        HashMap<String, String> kv = mainKeyStore.get(appId);

        String previousVal = null;
        if (kv.containsKey(key)) {
            previousVal = kv.get(key);
        }

        kv.put(key, val);
        return new KeyValueInfo(appId, key, previousVal, val);
    }
}
