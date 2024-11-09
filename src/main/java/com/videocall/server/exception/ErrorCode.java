package com.videocall.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được định nghĩa", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1003, "Lỗi không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Mật khẩu phải có ít nhât {min} kí tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Vui lòng đăng nhập để sử dụng tính năng này", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền truy cập tài nguyên này", HttpStatus.FORBIDDEN),
    USER_NAME_IS_REQUIRED(1008, "Tên đăng nhập không được để trống", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_REQUIRED(1009, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_NOT_CORRECT(1010, "Mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }



    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
