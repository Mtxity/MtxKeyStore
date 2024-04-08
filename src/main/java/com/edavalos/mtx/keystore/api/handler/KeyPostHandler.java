package com.edavalos.mtx.keystore.api.handler;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyPostHandler {

    @PostMapping(path = "/kv/store", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeValue(
            @RequestParam(value = "app_id", defaultValue = "_DEFAULT_PARAM_STR_")
            String appIdParam,
            @RequestParam(value = "key", defaultValue = "_DEFAULT_PARAM_STR_")
            String keyParam,
            @RequestParam(value = "value", defaultValue = "_DEFAULT_PARAM_STR_")
            String valueParam
    ) {
        return null;
    }
}
