package com.dev334.trace.model;

public class ApiResponse {
    private String message="Not found";
    private Integer status=404;

    public ApiResponse() {
        //empty constructor
    }

    public ApiResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
