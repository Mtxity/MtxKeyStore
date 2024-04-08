package com.edavalos.mtx.keystore.api.model;

import com.edavalos.mtx.keystore.Util;
import com.edavalos.mtx.keystore.api.HttpResponse;
import com.google.gson.Gson;

public class ApiResponse<T> {
    private static final Gson GSON = new Gson();

    private final String requestUri;
    private final String response;
    private final int statusCode;
    private final String message;
    private final String timestamp;
    private final T content;

    public ApiResponse(String requestUri, HttpResponse response, String message, T responseBody) {
        int responseCode = response.getCode();
        String responseCodeMsg = response.getMsg();

        this.requestUri = requestUri;
        this.response = responseCode + (!Util.isBlank(responseCodeMsg) ? " - " + responseCodeMsg : "");
        this.statusCode = responseCode;
        this.message = Util.isBlank(message) ? "" : message;
        this.timestamp = Util.getTimestamp();
        this.content = responseBody;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
