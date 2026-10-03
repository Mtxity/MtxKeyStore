package com.edavalos.mtx.keystore.api.model;

public class HashValueInfo {
    private final String hashName;
    private final String key;
    private final String value;

    public HashValueInfo(String hashName, String key, String value) {
        this.hashName = hashName;
        this.key = key;
        this.value = value;
    }
}
