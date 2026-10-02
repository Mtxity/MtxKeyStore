package com.edavalos.mtx.keystore;

import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
        loadKvsFromStorage();
        scheduleSaveToDbTasks();
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

    private static void loadKvsFromStorage() {
        if (SpringConfigLoader.getUseDb()) {
            loadRows(com.edavalos.mtx.keystore.db.KeyStoreLoader.loadKeyValues());
        }
        if (SpringConfigLoader.getUseNosql()) {
            loadRows(com.edavalos.mtx.keystore.nosql.KeyStoreLoader.loadKeyValues());
        }
        System.out.println("KV pairs have been loaded from configured storage");
    }

    private static void loadRows(List<? extends Record> rows) {
        for (Record row : rows) {
            String appId;
            String key;
            String val;
            String timestamp;
            if (row instanceof com.edavalos.mtx.keystore.db.KvRow dbRow) {
                appId = dbRow.appId();
                key = dbRow.key();
                val = dbRow.val();
                timestamp = dbRow.timestamp();
            } else if (row instanceof com.edavalos.mtx.keystore.nosql.KvRow nosqlRow) {
                appId = nosqlRow.appId();
                key = nosqlRow.key();
                val = nosqlRow.val();
                timestamp = nosqlRow.timestamp();
            } else {
                throw new IllegalArgumentException("Unsupported key-value row type: " + row.getClass());
            }

            if (!mainKeyStore.containsKey(appId)) {
                mainKeyStore.put(appId, new HashMap<>());
            }
            if (!mainKeyStoreTimestamps.containsKey(appId)) {
                mainKeyStoreTimestamps.put(appId, new HashMap<>());
            }

            mainKeyStore.get(appId).put(key, val);
            mainKeyStoreTimestamps.get(appId).put(key, timestamp);
        }
    }

    private static void saveKvsToDb() {
        if (!SpringConfigLoader.getUseDb() && !SpringConfigLoader.getUseNosql()) {
            return;
        }

        List<com.edavalos.mtx.keystore.db.KvRow> kvRows = new ArrayList<>();
        for (String appId : mainKeyStore.keySet()) {
            if (appId.equals(ApiConst.SAMPLE_APP_ID)) {
                continue;
            }

            HashMap<String, String> keyVals = mainKeyStore.get(appId);
            HashMap<String, String> keyTimestamps = mainKeyStoreTimestamps.get(appId);
            assert keyVals.size() == keyTimestamps.size();

            for (Map.Entry<String, String> keyVal : keyVals.entrySet()) {
                kvRows.add(new com.edavalos.mtx.keystore.db.KvRow(
                        appId,
                        keyVal.getKey(),
                        keyVal.getValue(),
                        keyTimestamps.get(keyVal.getKey())
                ));
            }
        }

        if (SpringConfigLoader.getUseDb()) {
            com.edavalos.mtx.keystore.db.KeyStoreRecorder.recordKeyValue(kvRows);
        }
        if (SpringConfigLoader.getUseNosql()) {
            com.edavalos.mtx.keystore.nosql.KeyStoreRecorder.recordKeyValue(
                    kvRows.stream()
                            .map(row -> new com.edavalos.mtx.keystore.nosql.KvRow(
                                    row.appId(), row.key(), row.val(), row.timestamp()))
                            .toList()
            );
        }
        System.out.println("KV pairs have been saved to configured storage");
    }

    private static void scheduleSaveToDbTasks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Saving KV pairs...");
                Thread.sleep(100);
                saveKvsToDb();
                System.out.println("Shutting down...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error halting for save to db on program exit: " + e);
            }
        }));

        int intervalInMillis = 1000 * 60 * SpringConfigLoader.getStoreInterval();
        new Timer("Save KV pairs to DB Timer").scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Saving KV pairs...");
                        saveKvsToDb();
                    }
                },
                intervalInMillis,
                intervalInMillis
        );
    }
}
