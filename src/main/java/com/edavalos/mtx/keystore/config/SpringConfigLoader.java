package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.DateTimeException;
import java.time.ZoneOffset;

@Configuration
@PropertySource("classpath:application.yml")
public class SpringConfigLoader {
    private static SpringConfig config;
    private static StringRedisTemplate redisTemplate;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Autowired
    public SpringConfigLoader(SpringConfig config, StringRedisTemplate redisTemplate) {
        SpringConfigLoader.config = config;
        SpringConfigLoader.redisTemplate = redisTemplate;
    }


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

    public static boolean getRequireAuthorization() {
        return config.requireAuthorization;
    }

    public static String getAuthToken() {
        return config.authToken;
    }

    public static boolean getIncludeTimestampsInGetAll() {
        return config.includeTimestampsInGetAll;
    }


    // --- config section: db

    public static boolean getUseDb() {
        return config.useDb;
    }

    public static int getStoreInterval() {
        return config.storeInterval;
    }

    public static boolean getInsertAllAtOnce() {
        return config.insertAllAtOnce;
    }


    // --- config section: nosql

    public static boolean getUseNosql() {
        return config.useNosql;
    }

    // --- config section: rcon
    // TODO: Validate this

    public static boolean getRconEnabled() {
        return config.rconEnabled;
    }

    public static String getRconHost() {
        return config.rconHost;
    }

    public static int getRconPort() {
        return config.rconPort;
    }

    public static String getRconPassword() {
        return config.rconPassword;
    }

    public static int getRconConnectTimeoutMillis() {
        return config.rconConnectTimeoutMillis;
    }

    public static int getRconReadTimeoutMillis() {
        return config.rconReadTimeoutMillis;
    }

    public static StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
