package com.edavalos.mtx.keystore.api;

import org.springframework.http.HttpStatus;

public enum HttpResponse {
    RESPONSE_200(200, HttpStatus.OK, "OK"),
    RESPONSE_201(201, HttpStatus.CREATED, "Created"),
    RESPONSE_400(400, HttpStatus.BAD_REQUEST, "Bad Request"),
    RESPONSE_401(401, HttpStatus.UNAUTHORIZED, "Unauthorized"),
    RESPONSE_403(403, HttpStatus.FORBIDDEN, "Forbidden"),
    RESPONSE_404(404, HttpStatus.NOT_FOUND, "Not Found"),
    RESPONSE_502(502, HttpStatus.BAD_GATEWAY, "Bad Gateway"),
    RESPONSE_503(503, HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable");

    private final int code;
    private final HttpStatus httpStatus;
    private final String msg;

    HttpResponse(int code, HttpStatus status, String details) {
        this.code = code;
        this.httpStatus = status;
        this.msg = details;
    }

    public int getCode() {
        return this.code;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getMsg() {
        return this.msg;
    }
}
