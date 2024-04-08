package com.edavalos.mtx.keystore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.DateTimeException;
import java.time.ZoneOffset;

@Configuration
@ConfigurationProperties(prefix = "app")
public class SpringConfig {
    private String timezone;

    public ZoneOffset getTimezone() {
        ZoneOffset zoneOffset;
        try {
            zoneOffset = ZoneOffset.of(this.timezone);
        } catch (DateTimeException dte) {
            System.err.println("Config contains unrecognized value for 'app.timezone': '" + this.timezone + "' (defaulting to UTC)");
            return ZoneOffset.UTC;
        }
        return zoneOffset;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
