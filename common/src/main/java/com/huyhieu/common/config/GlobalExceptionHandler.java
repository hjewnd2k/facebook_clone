package com.huyhieu.common.config;

import com.huyhieu.common.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

// @ControllerAdvice này sẽ được áp dụng cho bất kỳ service nào import module
@ControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý các lỗi chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Bạn có thể thêm các @ExceptionHandler khác cho các lỗi cụ thể
    // (ví dụ: ValidationException, NotFoundException...)
}
