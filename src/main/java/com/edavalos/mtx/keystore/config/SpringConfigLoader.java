package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
public class SpringConfigLoader {
    private final SpringConfig springConfig;

    @Autowired
    public SpringConfigLoader(SpringConfig springConfig) {
        this.springConfig = springConfig;
    }


    private static SpringConfigLoader config;


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
