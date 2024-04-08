package com.edavalos.mtx.keystore.api.model;

public class KeyValueInfo {

    private final String appId;
    private final String key;
    private final String previousValue;
    private final String newValue;

    public KeyValueInfo(String appId, String key, String previousValue, String newValue) {
        this.appId = appId;
        this.key = key;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }
}
