package com.edavalos.mtx.keystore.db;

public record KvRow(
        String appId,
        String key,
        String val,
        String timestamp
) { }
