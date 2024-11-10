package com.videocall.server.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.videocall.server.dto.ApiResponse;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalSocketExceptionHandler {
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageExceptionHandler(SocketException.class)
    public ResponseEntity<ApiResponse> handleAppException(SocketException e) {
        ErrorCode errorCode = e.getErrorCode();

        // Tạo thông báo lỗi
        ApiResponse errorResponse = ApiResponse.builder()
                .message(e.getMessage())
                .code(errorCode.getCode())
                .build();

        // Gửi thông báo lỗi tới /user/{userId}/topic/errors
        simpMessagingTemplate.convertAndSendToUser(e.getUserId(), "/topic/errors", errorResponse);

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
