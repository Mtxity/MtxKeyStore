package com.edavalos.mtx.keystore.nosql;

import java.util.Map;

/**
 * Example accessors for the existing ContourMC options hash.
 */
public final class ContourMcOptionsExample {
    private static final String OPTIONS_HASH = "contourmc:options";

    private ContourMcOptionsExample() { }

    /**
     * Gets one value from contourmc:options.
     */
    public static String getOption(String keyName) {
        return ValkeyHashStore.get(OPTIONS_HASH, keyName);
    }

    /**
     * Gets all key/value pairs from contourmc:options.
     */
    public static Map<Object, Object> getAllOptions() {
        return ValkeyHashStore.getAll(OPTIONS_HASH);
    }

    /**
     * Sets a value in contourmc:options, replacing the existing value when the key exists.
     */
    public static void setOption(String keyName, String value) {
        ValkeyHashStore.set(OPTIONS_HASH, keyName, value);
    }
}
