package com.edavalos.mtx.keystore;

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
    public static HashMap<String, HashMap<String, String>> mainKeyStore;

    public static void main(String[] args) {
        initMainKeyStore();
        ConfigurableApplicationContext apiServer = SpringApplication.run(MtxKeyStore.class, args);
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

        HashMap<String, String> sampleKS = new HashMap<>();
        sampleKS.put("_SAMPLE_KEY_", "_SAMPLE_VALUE_");

        mainKeyStore.put("_SAMPLE_APP_ID_", sampleKS);
    }
}
