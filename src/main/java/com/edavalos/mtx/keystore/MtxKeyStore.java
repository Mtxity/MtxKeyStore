package com.edavalos.mtx.keystore;

import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import com.edavalos.mtx.keystore.db.KeyStoreLoader;
import com.edavalos.mtx.keystore.db.KvRow;
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
    public static ConfigurableApplicationContext apiServer;

    // HashMap< App ID, HashMap< Key, Value >>
    public static HashMap<String, HashMap<String, String>> mainKeyStore;
    // HashMap< App ID, HashMap< Key, Last Updated Date >>
    public static HashMap<String, HashMap<String, String>> mainKeyStoreTimestamps;

    public static void main(String[] args) {
        apiServer = SpringApplication.run(MtxKeyStore.class, args);

        initMainKeyStore();
        loadKvsFromDb();
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

    private static void loadKvsFromDb() {
        if (!SpringConfigLoader.getUseDb()) {
            return;
        }

        for (KvRow kvRow : KeyStoreLoader.loadKeyValues()) {
            String appId = kvRow.appId();
            if (!mainKeyStore.containsKey(appId)) {
                mainKeyStore.put(appId, new HashMap<>());
            }
            if (!mainKeyStoreTimestamps.containsKey(appId)) {
                mainKeyStoreTimestamps.put(appId, new HashMap<>());
            }

            mainKeyStore.get(appId).put(kvRow.key(), kvRow.val());
            mainKeyStoreTimestamps.get(appId).put(kvRow.key(), kvRow.timestamp());
        }
        System.out.println("KV pairs have been loaded from database");
    }
}
