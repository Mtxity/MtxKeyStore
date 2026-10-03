package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.api.model.HashValueInfo;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import com.edavalos.mtx.keystore.nosql.ValkeyHashStore;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HashGetHandler {

    @GetMapping(path = "/hash/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getValue(
            @RequestHeader(value = "Authorization", defaultValue = ApiConst.DEFAULT_AUTH)
            String authHeader,
            @RequestParam(value = "hash_name", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String hashNameParam,
            @RequestParam(value = "key", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String keyParam
    ) {
        ResponseEntity<String> authFailure = validateAuthorization(authHeader);
        if (authFailure != null) {
            return authFailure;
        }

        String[] missingParams = Util.getMissingParams(Map.of(
                "hash_name", hashNameParam,
                "key", keyParam
        ));
        if (missingParams.length > 0) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_400.getHttpStatus())
                    .body(new ApiResponse<HashValueInfo>(
                            "/hash/get",
                            HttpResponse.RESPONSE_400,
                            "Missing parameters: " + String.join(", ", missingParams),
                            null
                    ).toString());
        }

        String value = ValkeyHashStore.get(hashNameParam, keyParam);
        if (value == null) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_404.getHttpStatus())
                    .body(new ApiResponse<HashValueInfo>(
                            "/hash/get",
                            HttpResponse.RESPONSE_404,
                            "no value found with key: '" + keyParam + "' in hash: '" + hashNameParam + "'",
                            null
                    ).toString());
        }

        return ResponseEntity
                .status(HttpResponse.RESPONSE_200.getHttpStatus())
                .body(new ApiResponse<HashValueInfo>(
                        "/hash/get",
                        HttpResponse.RESPONSE_200,
                        "Value successfully retrieved",
                        new HashValueInfo(hashNameParam, keyParam, value)
                ).toString());
    }

    private ResponseEntity<String> validateAuthorization(String authHeader) {
        if (!SpringConfigLoader.getRequireAuthorization()) {
            return null;
        }
        if (Util.isBlank(authHeader) || authHeader.equalsIgnoreCase(ApiConst.DEFAULT_AUTH)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<HashValueInfo>(
                            "/hash/get",
                            HttpResponse.RESPONSE_403,
                            "no authentication provided",
                            null
                    ).toString());
        }
        if (!SpringConfigLoader.getAuthToken().equals(authHeader)) {
            return ResponseEntity
                    .status(HttpResponse.RESPONSE_403.getHttpStatus())
                    .body(new ApiResponse<HashValueInfo>(
                            "/hash/get",
                            HttpResponse.RESPONSE_403,
                            "Authentication invalid",
                            null
                    ).toString());
        }
        return null;
    }
}
