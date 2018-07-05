package io.infinite.datasync.common;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {



    private int code;
    @JsonProperty("msg")
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ApiResponse() {
    }

    private ApiResponse(T data) {
        this.data = data;
    }

    private ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> create(T t) {
        return new ApiResponse<T>(t);
    }

    public static <T> ApiResponse<T> createSuccessByMsg(String message) {
        return new ApiResponse<T>(HttpStatus.OK.value(), message, null);
    }


    public static <T> ApiResponse<T> createFail(String message) {
        return new ApiResponse<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

}