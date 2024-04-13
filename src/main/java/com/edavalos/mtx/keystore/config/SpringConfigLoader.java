package com.edavalos.mtx.keystore.config;

import java.time.ZoneOffset;

public class SpringConfigLoader {

    public static ZoneOffset getTimezone() {
        return ZoneOffset.UTC;
    }

    public static String getAuthToken() {
        return "basic e35acfe4e0b54d488b687a64914197ad";
    }

    public static int getStoreInterval() {
        return 5;
    }

    public static boolean getIncludeTimestampsInGetAll() {
        return true;
    }
}
