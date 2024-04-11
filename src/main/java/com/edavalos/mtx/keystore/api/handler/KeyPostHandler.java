package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.api.model.KeyValueInfo;
import com.edavalos.mtx.keystore.api.model.ValueInfo;
import com.edavalos.mtx.keystore.api.processor.KeyPostProcessor;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class KeyPostHandler {

    @PostMapping(path = "/kv/store", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeValue(
            @RequestHeader(value = "Authorization", defaultValue = ApiConst.DEFAULT_AUTH)
            String authHeader,
            @RequestParam(value = "app_id", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String appIdParam,
            @RequestParam(value = "key", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String keyParam,
            @RequestParam(value = "value", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String valueParam
    ) {
        if (Util.isBlank(authHeader) || authHeader.equalsIgnoreCase(ApiConst.DEFAULT_AUTH)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/store",
                            HttpResponse.RESPONSE_403,
                            "no authentication provided",
                            null
                    ).toString());
        }

        if (!SpringConfigLoader.getAuthToken().equals(authHeader)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/store",
                            HttpResponse.RESPONSE_403,
                            "Authentication invalid",
                            null
                    ).toString());
        }

        String[] missingParams = Util.getMissingParams(Map.of(
                "app_id", appIdParam,
                "key", keyParam,
                "value", valueParam
        ));
        if (missingParams.length > 0) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_400.getHttpStatus())
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/store",
                            HttpResponse.RESPONSE_400,
                            "Missing parameters: " + String.join(", ", missingParams),
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
