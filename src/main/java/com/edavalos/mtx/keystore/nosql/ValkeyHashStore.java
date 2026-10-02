package com.edavalos.mtx.keystore.nosql;

import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * Reads and writes string fields in arbitrary Valkey hashes.
 */
public final class ValkeyHashStore {
    private ValkeyHashStore() { }

    /**
     * Reads one field from a hash using HGET semantics.
     */
    public static String get(String hashName, String fieldName) {
        Object value = redisTemplate().opsForHash().get(hashName, fieldName);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * Reads multiple fields from a hash using HMGET semantics. Returned values
     * have the same order as the requested field names; missing fields are null.
     */
    public static List<String> get(String hashName, List<String> fieldNames) {
        List<Object> hashFields = fieldNames.stream()
                .map(fieldName -> (Object) fieldName)
                .toList();
        return redisTemplate().opsForHash().multiGet(hashName, hashFields).stream()
                .map(value -> value == null ? null : String.valueOf(value))
                .toList();
    }

    /**
     * Reads every field in a hash using HGETALL semantics.
     */
    public static Map<Object, Object> getAll(String hashName) {
        return redisTemplate().opsForHash().entries(hashName);
    }

    /**
     * Writes one field to a hash using HSET semantics.
     */
    public static void set(String hashName, String fieldName, String value) {
        redisTemplate().opsForHash().put(hashName, fieldName, value);
    }

    private static StringRedisTemplate redisTemplate() {
        return SpringConfigLoader.getRedisTemplate();
    }
}
