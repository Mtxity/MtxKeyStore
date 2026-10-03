package com.edavalos.mtx.keystore.api.model;

public class HashValueChangeInfo {
    private final String hashName;
    private final String key;
    private final String previousValue;
    private final String newValue;

    public HashValueChangeInfo(String hashName, String key, String previousValue, String newValue) {
        this.hashName = hashName;
        this.key = key;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }
}
