package com.videocall.server.exception;

import com.videocall.server.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalSocketExceptionHandler {


    @MessageExceptionHandler(AppException.class)
    @SendToUser("/topic/errors")
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getCode()).body(ApiResponse.builder()
                .message(e.getMessage())
                .code(errorCode.getCode())
                .build());
    }
}
