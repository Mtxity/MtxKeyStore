package com.edavalos.mtx.keystore.nosql;

import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import com.google.gson.Gson;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class KeyStoreLoader {
    private static final Gson GSON = new Gson();

    private KeyStoreLoader() { }

    public static List<KvRow> loadKeyValues() {
        StringRedisTemplate redisTemplate = SpringConfigLoader.getRedisTemplate();
        Set<String> appIds = redisTemplate.opsForSet().members(NoSqlConst.APP_IDS_KEY);
        if (appIds == null || appIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<KvRow> results = new ArrayList<>();
        for (String appId : appIds) {
            Map<Object, Object> keyValues = redisTemplate.opsForHash()
                    .entries(NoSqlConst.getAppKey(appId));
            for (Map.Entry<Object, Object> keyValue : keyValues.entrySet()) {
                KvRow row = GSON.fromJson(String.valueOf(keyValue.getValue()), KvRow.class);
                results.add(new KvRow(appId, String.valueOf(keyValue.getKey()), row.val(), row.timestamp()));
            }
        }

        System.out.println("Successfully queried and loaded all KV pairs from Valkey");
        return results;
    }
}
