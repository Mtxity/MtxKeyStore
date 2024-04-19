package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.ZoneOffset;

@Component
public class SpringConfigLoader {
    private static final SpringConfigLoader config = new SpringConfigLoader();

    @Value("${app.timezone}")
    private String timezone;

    @Value("${api.authToken}")
    private String authToken;

    @Value("${api.includeTimestampsInGetAll}")
    private boolean includeTimestampsInGetAll;

    @Value("${db.storeInterval}")
    private int storeInterval;

    private SpringConfigLoader() { }


    // --- config section: app

    public static ZoneOffset getTimezone() {
        ZoneOffset zoneOffset;
        try {
            zoneOffset = ZoneOffset.of(config.timezone);
        } catch (DateTimeException dte) {
            System.err.println("Config contains unrecognized value for 'app.timezone': '" + config.timezone + "' (defaulting to UTC)");
            config.timezone = ZoneOffset.UTC.getId();
            return ZoneOffset.UTC;
        }
        return zoneOffset;
    }


    // --- config section: api

    public static String getAuthToken() {
        return config.authToken;
    }

    public static boolean getIncludeTimestampsInGetAll() {
        return config.includeTimestampsInGetAll;
    }


    // --- config section: db

    public static int getStoreInterval() {
        return config.storeInterval;
    }
}
