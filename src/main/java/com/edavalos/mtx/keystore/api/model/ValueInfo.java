package com.edavalos.mtx.keystore.api.model;

public class ValueInfo {

    private final String appId;
    private final String key;
    private final String value;
    // @TODO: Add last modified timestamp to response (i.e. store last time modified)
//    private final String lastModifiedTimestamp;

    public ValueInfo(String appId, String key, String value) {
        this.appId = appId;
        this.key = key;
        this.value = value;
    }
}
