package com.edavalos.mtx.keystore.api.model;

public class ApiResponse<T> {

    private final String requestUri;
    private final String response;
    private final int statusCode;
//    private final String timestamp;
    private final T content;

    public ApiResponse(String requestUri, int responseCode, String responseMsg, T responseBody) {
        boolean hasMsg = (responseMsg != null) && !responseMsg.isEmpty() && !responseMsg.isBlank(); // @TODO: Make a isBlank() util

        this.requestUri = requestUri;
        this.response = responseCode + (hasMsg ? " - " + responseMsg : ""); // @TODO: Add response message from http code
        this.statusCode = responseCode;
        this.content = responseBody;
    }
}
