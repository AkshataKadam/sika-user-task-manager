package com.sika.demo.base.utils;

import com.sika.demo.base.model.ApiResponseDTO;
public class ResponseBuilder {

    public static <T> ApiResponseDTO<T> success(String code, String message, T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(String code, String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }
}
