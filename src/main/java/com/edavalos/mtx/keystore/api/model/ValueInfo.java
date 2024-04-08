package com.edavalos.mtx.keystore.api.model;

public class ValueInfo {

    private final String appId;
    private final String key;
    private final String value;
    private final String lastModifiedTimestamp;

    public ValueInfo(String appId, String key, String value, String lastModifiedTimestamp) {
        this.appId = appId;
        this.key = key;
        this.value = value;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }
}
