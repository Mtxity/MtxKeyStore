package com.edavalos.mtx.keystore.nosql;

import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import com.google.gson.Gson;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class KeyStoreRecorder {
    private static final Gson GSON = new Gson();

    private KeyStoreRecorder() { }

    public static void recordKeyValue(KvRow kvRow) {
        recordKeyValue(List.of(kvRow));
    }

    public static void recordKeyValue(List<KvRow> kvRows) {
        if (kvRows.isEmpty()) {
            return;
        }

        StringRedisTemplate redisTemplate = SpringConfigLoader.getRedisTemplate();
        Map<String, Map<String, String>> valuesByApp = new HashMap<>();
        for (KvRow kvRow : kvRows) {
            valuesByApp.computeIfAbsent(kvRow.appId(), ignored -> new HashMap<>())
                    .put(kvRow.key(), GSON.toJson(kvRow));
        }

        for (Map.Entry<String, Map<String, String>> appValues : valuesByApp.entrySet()) {
            redisTemplate.opsForHash().putAll(
                    NoSqlConst.getAppKey(appValues.getKey()),
                    appValues.getValue()
            );
            redisTemplate.opsForSet().add(NoSqlConst.APP_IDS_KEY, appValues.getKey());
        }

        System.out.println("KV pairs stored in Valkey");
    }

    public static void deleteKeyValue(String appId, String key) {
        SpringConfigLoader.getRedisTemplate()
                .opsForHash()
                .delete(NoSqlConst.getAppKey(appId), key);
        System.out.println("KV pair deleted from Valkey");
    }
}
