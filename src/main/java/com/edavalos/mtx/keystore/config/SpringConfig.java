package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringConfig {

    @Value("${app.timezone}")
    String timezone;

    @Value("${api.requireAuthorization}")
    boolean requireAuthorization;

    @Value("${api.authToken}")
    String authToken;

    @Value("${api.includeTimestampsInGetAll}")
    boolean includeTimestampsInGetAll;

    @Value("${db.storeInterval}")
    int storeInterval;

    SpringConfig() { }
}
