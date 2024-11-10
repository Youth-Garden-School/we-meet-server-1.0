package com.videocall.server.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketException extends RuntimeException {

    public SocketException(ErrorCode errorCode, String userId) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.userId = userId;
    }

    private ErrorCode errorCode;
    private String userId;
}
