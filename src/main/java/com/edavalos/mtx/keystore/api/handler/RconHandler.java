package com.edavalos.mtx.keystore.api.handler;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.edavalos.mtx.keystore.api.model.ApiResponse;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;
import com.edavalos.mtx.keystore.rcon.RconClient;
import com.edavalos.mtx.keystore.rcon.RconException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RconHandler {
    // TODO: Validate this
    @PostMapping(path = "/rcon/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> send(
            @RequestHeader(value = "Authorization", defaultValue = ApiConst.DEFAULT_AUTH) String authHeader,
            @RequestParam(value = "message", defaultValue = ApiConst.DEFAULT_PARAM_STR) String message) {
        ResponseEntity<String> authFailure = validateAuthorization(authHeader);
        if (authFailure != null) {
            return authFailure;
        }

        String[] missingParams = Util.getMissingParams(Map.of("message", message));
        if (missingParams.length > 0) {
            return response(HttpResponse.RESPONSE_400, "Missing parameters: message", null);
        }
        if (!SpringConfigLoader.getRconEnabled()) {
            return response(HttpResponse.RESPONSE_503, "RCON is disabled", null);
        }

        try {
            String result = new RconClient(
                    SpringConfigLoader.getRconHost(), SpringConfigLoader.getRconPort(),
                    SpringConfigLoader.getRconPassword(),
                    SpringConfigLoader.getRconConnectTimeoutMillis(),
                    SpringConfigLoader.getRconReadTimeoutMillis()).send(message);
            return response(HttpResponse.RESPONSE_200, "RCON command sent", result);
        } catch (RconException e) {
            return response(HttpResponse.RESPONSE_502, e.getMessage(), null);
        }
    }

    private ResponseEntity<String> validateAuthorization(String authHeader) {
        if (!SpringConfigLoader.getRequireAuthorization()) {
            return null;
        }
        if (Util.isBlank(authHeader) || authHeader.equalsIgnoreCase(ApiConst.DEFAULT_AUTH)) {
            return response(HttpResponse.RESPONSE_403, "no authentication provided", null);
        }
        if (!SpringConfigLoader.getAuthToken().equals(authHeader)) {
            return response(HttpResponse.RESPONSE_403, "Authentication invalid", null);
        }
        return null;
    }

    private ResponseEntity<String> response(HttpResponse status, String message, String content) {
        return ResponseEntity.status(status.getHttpStatus())
                .body(new ApiResponse<String>("/rcon/send", status, message, content).toString());
    }
}
