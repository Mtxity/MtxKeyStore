package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.api.model.ValueInfo;
import com.edavalos.mtx.keystore.api.processor.KeyGetProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyGetHandler {

    @GetMapping(path = "/kv/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getValue(
            @RequestParam(value = "app_id", defaultValue = "_DEFAULT_PARAM_STR_")
            String appIdParam,
            @RequestParam(value = "key", defaultValue = "_DEFAULT_PARAM_STR_")
            String keyParam
    ) {
        ValueInfo vInfo = KeyGetProcessor.getValue(appIdParam, keyParam);

        if (vInfo == null) {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<ValueInfo>(
                            "/kv/get",
                            404,
                            "no value found with key: '" + keyParam + "'",
                            null
                    ).toString());
        }

        return ResponseEntity
                .status(200)
                .body(new ApiResponse<ValueInfo>(
                        "/kv/get",
                        200,
                        "Value successfully retrieved",
                        vInfo
                ).toString());
    }
}
