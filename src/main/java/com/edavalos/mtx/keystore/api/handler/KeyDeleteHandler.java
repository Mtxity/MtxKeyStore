package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.api.model.ValueInfo;
import com.edavalos.mtx.keystore.api.processor.KeyDeleteProcessor;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class KeyDeleteHandler {

    @DeleteMapping(path = "/kv/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteValue(
            @RequestHeader(value = "Authorization", defaultValue = ApiConst.DEFAULT_AUTH)
            String authHeader,
            @RequestParam(value = "app_id", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String appIdParam,
            @RequestParam(value = "key", defaultValue = ApiConst.DEFAULT_PARAM_STR)
            String keyParam
    ) {
        if (SpringConfigLoader.getRequireAuthorization()) {
            if (Util.isBlank(authHeader) || authHeader.equalsIgnoreCase(ApiConst.DEFAULT_AUTH)) {
                return response(HttpResponse.RESPONSE_403, "no authentication provided", null);
            }
            if (!SpringConfigLoader.getAuthToken().equals(authHeader)) {
                return response(HttpResponse.RESPONSE_403, "Authentication invalid", null);
            }
        }

        String[] missingParams = Util.getMissingParams(Map.of(
                "app_id", appIdParam,
                "key", keyParam
        ));
        if (missingParams.length > 0) {
            return response(HttpResponse.RESPONSE_400,
                    "Missing parameters: " + String.join(", ", missingParams), null);
        }

        ValueInfo deletedValue = KeyDeleteProcessor.deleteValue(appIdParam, keyParam);
        if (deletedValue == null) {
            return response(HttpResponse.RESPONSE_404,
                    "no value found with key: '" + keyParam + "'", null);
        }

        if (SpringConfigLoader.getUseDb()) {
            com.edavalos.mtx.keystore.db.KeyStoreRecorder.deleteKeyValue(appIdParam, keyParam);
        }
        if (SpringConfigLoader.getUseNosql()) {
            com.edavalos.mtx.keystore.nosql.KeyStoreRecorder.deleteKeyValue(appIdParam, keyParam);
        }

        return response(HttpResponse.RESPONSE_200, "Value successfully deleted", deletedValue);
    }

    private ResponseEntity<String> response(HttpResponse status, String message, ValueInfo content) {
        return ResponseEntity.status(status.getHttpStatus())
                .body(new ApiResponse<ValueInfo>("/kv/delete", status, message, content).toString());
    }
}
