package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.ZoneOffset;

@Component
public final class SpringConfigLoader {
    private SpringConfigLoader() { }

    @Value("$(app.timezone)")
    private static String timezone;
    @Value("$(app.authToken)")
    private static String authToken;
    @Value("$(app.storeInterval)")
    private static int storeInterval;
    @Value("$(app.includeTimestampsInGetAll)")
    private static boolean includeTimestampsInGetAll;


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

    public static String getAuthToken() {
        return authToken;
    }

    public static int getStoreInterval() {
        return storeInterval;
    }

    public static boolean getIncludeTimestampsInGetAll() {
        return includeTimestampsInGetAll;
    }
}
