package com.edavalos.mtx.keystore;

import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@SpringBootApplication
@RestController
public class MtxKeyStore {
    // HashMap< App ID, HashMap< Key, Value >>
    public static HashMap<String, HashMap<String, String>> mainKeyStore;
    // HashMap< App ID, HashMap< Key, Last Updated Date >>
    public static HashMap<String, HashMap<String, String>> mainKeyStoreTimestamps;

    public static ConfigurableApplicationContext apiServer;

    public final SpringConfigLoader config;

    @Autowired
    public MtxKeyStore(SpringConfigLoader springConfigLoader) {
        this.config = springConfigLoader;
    }

    public static void main(String[] args) {
        initMainKeyStore();
        apiServer = SpringApplication.run(MtxKeyStore.class, args);
    }

    @GetMapping(path = "/healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hello(
            @RequestParam(value = "name", defaultValue = "client")
            String name
    ) {
        return ResponseEntity
                .status(200)
                .body("{\"sender\":\"" + name + "\",\"status\":\"alive\"}");
    }

    private static void initMainKeyStore() {
        mainKeyStore = new HashMap<>();
        mainKeyStoreTimestamps = new HashMap<>();

        HashMap<String, String> sampleKS = new HashMap<>();
        HashMap<String, String> sampleKSts = new HashMap<>();
        sampleKS.put(ApiConst.SAMPLE_KEY, ApiConst.SAMPLE_VALUE);
        sampleKSts.put(ApiConst.SAMPLE_KEY, Util.getTimestamp());

        mainKeyStore.put(ApiConst.SAMPLE_APP_ID, sampleKS);
        mainKeyStoreTimestamps.put(ApiConst.SAMPLE_APP_ID, sampleKSts);
    }
}
