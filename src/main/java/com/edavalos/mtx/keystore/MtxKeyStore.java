package com.edavalos.mtx.keystore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MtxKeyStore {

    public static void main(String[] args) {
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
}
