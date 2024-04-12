package com.edavalos.mtx.keystore.api.processor;

import com.edavalos.mtx.keystore.api.model.ValueInfo;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;

import java.util.Map;

import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStore;
import static com.edavalos.mtx.keystore.MtxKeyStore.mainKeyStoreTimestamps;

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
            String lastModifiedTimestamp = null;
            if (SpringConfigLoader.getIncludeTimestampsInGetAll()
                    && mainKeyStoreTimestamps.containsKey(appId)
                    && mainKeyStoreTimestamps.get(appId).containsKey(kvEntry.getValue())) {
                lastModifiedTimestamp = mainKeyStoreTimestamps.get(appId).get(kvEntry.getValue());
            }

            valueInfos[idx++] = new ValueInfo(null, kvEntry.getKey(), kvEntry.getValue(), lastModifiedTimestamp);
        }
        return valueInfos;
    }
}
