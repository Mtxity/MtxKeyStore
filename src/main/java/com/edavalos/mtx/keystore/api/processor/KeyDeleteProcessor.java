package com.edavalos.mtx.keystore.api.processor;

import com.edavalos.mtx.keystore.api.model.ValueInfo;

import java.util.HashMap;

import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStore;
import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStoreTimestamps;

public final class KeyDeleteProcessor {
    private KeyDeleteProcessor() { }

    public static ValueInfo deleteValue(String appId, String key) {
        if (!mainKeyStore.containsKey(appId) || !mainKeyStore.get(appId).containsKey(key)) {
            return null;
        }

        HashMap<String, String> values = mainKeyStore.get(appId);
        String value = values.remove(key);
        String timestamp = mainKeyStoreTimestamps.containsKey(appId)
                ? mainKeyStoreTimestamps.get(appId).remove(key)
                : null;

        if (values.isEmpty()) {
            mainKeyStore.remove(appId);
            mainKeyStoreTimestamps.remove(appId);
        }

        return new ValueInfo(appId, key, value, timestamp);
    }
}
