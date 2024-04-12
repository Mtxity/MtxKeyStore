package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
public final class SpringConfigLoader {
    private SpringConfigLoader() { }

    @Autowired
    private SpringConfig springConfig;

    private static SpringConfigLoader config;

    public static void loadConfig() {
        config = new SpringConfigLoader();
    }


    public static ZoneOffset getTimezone() {
        return config.springConfig.getTimezone();
    }

    public static String getAuthToken() {
        return config.springConfig.getAuthToken();
    }

    public static int getStoreInterval() {
        return config.springConfig.getStoreInterval();
    }

    public static boolean getIncludeTimestampsInGetAll() {
        return config.springConfig.getIncludeTimestampsInGetAll();
    }
}
