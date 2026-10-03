package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.api.model.HashValueChangeInfo;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import com.edavalos.mtx.keystore.nosql.ValkeyHashStore;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HashSetHandler {

    @PostMapping(path = "/hash/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setValue(
            @RequestHeader(value = "Authorization", defaultValue = ApiConst.DEFAULT_AUTH)
            String authHeader,
            @RequestParam(value = "hash_name", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String hashNameParam,
            @RequestParam(value = "key", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String keyParam,
            @RequestParam(value = "value", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String valueParam
    ) {
        ResponseEntity<String> authFailure = validateAuthorization(authHeader);
        if (authFailure != null) {
            return authFailure;
        }

        String[] missingParams = Util.getMissingParams(Map.of(
                "hash_name", hashNameParam,
                "key", keyParam,
                "value", valueParam
        ));
        if (missingParams.length > 0) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_400.getHttpStatus())
                    .body(new ApiResponse<HashValueChangeInfo>(
                            "/hash/set",
                            HttpResponse.RESPONSE_400,
                            "Missing parameters: " + String.join(", ", missingParams),
                            null
                    ).toString());
        }

        String previousValue = ValkeyHashStore.get(hashNameParam, keyParam);
        ValkeyHashStore.set(hashNameParam, keyParam, valueParam);

        return ResponseEntity
                .status(HttpResponse.RESPONSE_200.getHttpStatus())
                .body(new ApiResponse<HashValueChangeInfo>(
                        "/hash/set",
                        HttpResponse.RESPONSE_200,
                        "Value successfully stored",
                        new HashValueChangeInfo(hashNameParam, keyParam, previousValue, valueParam)
                ).toString());
    }

    private ResponseEntity<String> validateAuthorization(String authHeader) {
        if (!SpringConfigLoader.getRequireAuthorization()) {
            return null;
        }
        if (Util.isBlank(authHeader) || authHeader.equalsIgnoreCase(ApiConst.DEFAULT_AUTH)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<HashValueChangeInfo>(
                            "/hash/set",
                            HttpResponse.RESPONSE_403,
                            "no authentication provided",
                            null
                    ).toString());
        }
        if (!SpringConfigLoader.getAuthToken().equals(authHeader)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<HashValueChangeInfo>(
                            "/hash/set",
                            HttpResponse.RESPONSE_403,
                            "Authentication invalid",
                            null
                    ).toString());
        }
        return null;
    }
}
