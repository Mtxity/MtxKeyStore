package com.edavalos.mtx.keystore.nosql;

public record KvRow(
        String appId,
        String key,
        String val,
        String timestamp
) { }
