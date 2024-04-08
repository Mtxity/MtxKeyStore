package com.edavalos.mtx.keystore.api.model;

import com.edavalos.mtx.keystore.Util;
import com.google.gson.Gson;

public class ApiResponse<T> {
    private static final Gson GSON = new Gson();

    private final String requestUri;
    private final String response;
    private final int statusCode;
    private final String timestamp;
    private final T content;

    public ApiResponse(String requestUri, int responseCode, String responseMsg, T responseBody) {
        this.requestUri = requestUri;
        this.response = responseCode + (!Util.isBlank(responseMsg) ? " - " + responseMsg : ""); // @TODO: Add response message from http code
        this.statusCode = responseCode;
        this.timestamp = Util.getTimestamp();
        this.content = responseBody;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
