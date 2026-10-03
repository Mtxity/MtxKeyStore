package com.edavalos.mtx.keystore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringConfig {
    private transient final String SAMPLE_API_KEY = "changeme";

    @Value("${app.timezone}")
    String timezone;

    @Value("${api.requireAuthorization}")
    boolean requireAuthorization;

    @Value("${api.authToken:" + SAMPLE_API_KEY + "}")
    String authToken;

    @Value("${api.includeTimestampsInGetAll}")
    boolean includeTimestampsInGetAll;

    @Value("${db.useDb}")
    boolean useDb;

    @Value("${db.storeInterval}")
    int storeInterval;

    @Value("${db.insertAllAtOnce}")
    boolean insertAllAtOnce;

    @Value("${nosql.useNosql}")
    boolean useNosql;

    @Value("${rcon.enabled:false}")
    boolean rconEnabled;

    @Value("${rcon.host:localhost}")
    String rconHost;

    @Value("${rcon.port:25575}")
    int rconPort;

    @Value("${rcon.password:}")
    String rconPassword;

    @Value("${rcon.connectTimeoutMillis:5000}")
    int rconConnectTimeoutMillis;

    @Value("${rcon.readTimeoutMillis:5000}")
    int rconReadTimeoutMillis;

    SpringConfig() { }
}
