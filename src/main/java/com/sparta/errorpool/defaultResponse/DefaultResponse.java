package com.sparta.errorpool.defaultResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultResponse<T> {

    private int statusCode;
    private String responseMessage;
    @Builder.Default
    private boolean successYn = true;
    private T data;


    public DefaultResponse(final boolean successYn ,final int statusCode, final String responseMessage) {
        this.successYn = successYn;
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static<T> DefaultResponse<T> res(final boolean successYn, final int statusCode, final String responseMessage) {
        return res(successYn, statusCode, responseMessage, null);
    }

    public static<T> DefaultResponse<T> res(boolean successYn, final int statusCode, final String responseMessage, final T t) {
        return DefaultResponse.<T>builder()
                .data(t)
                .successYn(successYn)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}

