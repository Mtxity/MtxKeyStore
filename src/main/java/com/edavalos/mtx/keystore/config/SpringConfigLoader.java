package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.ZoneOffset;

@Component
public class SpringConfigLoader {

    @Value("${app.timezone}")
    private static String timezone;

    @Value("${api.authToken}")
    private static String authToken;

    @Value("${api.includeTimestampsInGetAll}")
    private static boolean includeTimestampsInGetAll;

    @Value("${db.storeInterval}")
    private static int storeInterval;


    // --- config section: app

    public static ZoneOffset getTimezone() {
        ZoneOffset zoneOffset;
        try {
            zoneOffset = ZoneOffset.of(timezone);
        } catch (DateTimeException dte) {
            System.err.println("Config contains unrecognized value for 'app.timezone': '" + timezone + "' (defaulting to UTC)");
            timezone = ZoneOffset.UTC.getId();
            return ZoneOffset.UTC;
        }
        return zoneOffset;
    }


    // --- config section: api

    public static String getAuthToken() {
        return authToken;
    }

    public static boolean getIncludeTimestampsInGetAll() {
        return includeTimestampsInGetAll;
    }


    // --- config section: db

    public static int getStoreInterval() {
        return storeInterval;
    }
}
