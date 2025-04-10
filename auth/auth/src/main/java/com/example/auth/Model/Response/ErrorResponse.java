package com.example.auth.Model.Response;

public class ErrorResponse {

    private int statusCode;
    private String exception;
    private String msg;

    public ErrorResponse(int statusCode, String msg, String exception) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.exception = exception;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
