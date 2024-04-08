package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.api.model.KeyValueInfo;
import com.edavalos.mtx.keystore.api.model.ValueInfo;
import com.edavalos.mtx.keystore.api.processor.KeyPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyPostHandler {

    @PostMapping(path = "/kv/store", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeValue(
            @RequestHeader(value = "Authorization", defaultValue = "_DEFAULT_AUTH_")
            String authHeader,
            @RequestParam(value = "app_id", defaultValue = "_DEFAULT_PARAM_STR_")
            String appIdParam,
            @RequestParam(value = "key", defaultValue = "_DEFAULT_PARAM_STR_")
            String keyParam,
            @RequestParam(value = "value", defaultValue = "_DEFAULT_PARAM_STR_")
            String valueParam
    ) {
        if (Util.isBlank(authHeader)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/store",
                            HttpResponse.RESPONSE_403,
                            "no authentication provided",
                            null
                    ).toString());
        }

        KeyValueInfo kvInfo = KeyPostProcessor.storeValue(appIdParam, keyParam, valueParam);
        return ResponseEntity
                .status(HttpResponse.RESPONSE_200.getHttpStatus())
                .body(new ApiResponse<KeyValueInfo>(
                        "/kv/store",
                        HttpResponse.RESPONSE_200,
                        "key successfully stored",
                        kvInfo
                ).toString());
    }
}
