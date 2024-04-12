package com.edavalos.mtx.keystore.api.processor;

import com.edavalos.mtx.keystore.api.model.ValueInfo;

import java.util.Map;

import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStore;

public final class KeyGetAllProcessor {
    private KeyGetAllProcessor() { }

    public static ValueInfo[] getAllValues(String appId) {
        if (!mainKeyStore.containsKey(appId)) {
            return new ValueInfo[0];
        }
        Map<String, String> kv = mainKeyStore.get(appId);

        ValueInfo[] valueInfos = new ValueInfo[kv.size()];
        int idx = 0;
        for (Map.Entry<String, String> kvEntry : kv.entrySet()) {
            // @TODO: Add config option to include lastModifiedTimestamp
            valueInfos[idx++] = new ValueInfo(null, kvEntry.getKey(), kvEntry.getValue(), null);
        }
        return valueInfos;
    }
}
