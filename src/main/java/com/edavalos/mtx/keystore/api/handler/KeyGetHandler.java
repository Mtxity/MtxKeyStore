package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.api.model.ValueInfo;
import com.edavalos.mtx.keystore.api.processor.KeyGetProcessor;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyGetHandler {

    @GetMapping(path = "/kv/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getValue(
            @RequestHeader(value = "Authorization", defaultValue = "_DEFAULT_AUTH_")
            String authHeader,
            @RequestParam(value = "app_id", defaultValue = "_DEFAULT_PARAM_STR_")
            String appIdParam,
            @RequestParam(value = "key", defaultValue = "_DEFAULT_PARAM_STR_")
            String keyParam
    ) {
        if (Util.isBlank(authHeader)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/get",
                            HttpResponse.RESPONSE_403,
                            "no authentication provided",
                            null
                    ).toString());
        }

        if (!SpringConfigLoader.getAuthToken().equals(authHeader)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/get",
                            HttpResponse.RESPONSE_403,
                            "Authentication invalid",
                            null
                    ).toString());
        }

        ValueInfo vInfo = KeyGetProcessor.getValue(appIdParam, keyParam);

        if (vInfo == null) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_404.getHttpStatus())
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/get",
                            HttpResponse.RESPONSE_404,
                            "no value found with key: '" + keyParam + "'",
                            null
                    ).toString());
        }

        return ResponseEntity
                .status(HttpResponse.RESPONSE_200.getHttpStatus())
                .body(new ApiResponse<ValueInfo>(
                        "/kv/get",
                        HttpResponse.RESPONSE_200,
                        "Value successfully retrieved",
                        vInfo
                ).toString());
    }
}
