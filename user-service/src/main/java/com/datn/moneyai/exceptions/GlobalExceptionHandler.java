package com.datn.moneyai.exceptions;

import com.datn.moneyai.models.global.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi do người dùng gây ra
    @ExceptionHandler(UserMessageException.class)
    public ResponseEntity<ApiResult<Object>> handleUserMessageException(UserMessageException ex) {
        return ResponseEntity.badRequest().body(ApiResult.fail(ex.getMessage()));
    }

    // Xử lý lỗi validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResult<Object> apiResult = new ApiResult<>();
        apiResult.setStatus(false);
        apiResult.setUserMessage("Dữ liệu đầu vào không hợp lệ");
        apiResult.setData(errors);
        return ResponseEntity.badRequest().body(apiResult);
    }

    // Xử lý lỗi không tìm thấy tài nguyên
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResult<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ApiResult.fail(ex.getMessage()));
    }

    // Xử lý lỗi không tìm thấy đường dẫn
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResult<Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResult.fail("Không tìm thấy đường dẫn bạn yêu cầu (404)"));
    }

    // Xử lý lỗi không hỗ trợ phương thức HTTP
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResult<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(405)
                .body(ApiResult.fail("Phương thức HTTP không được hỗ trợ cho API này"));
    }

    // Xử lý lỗi chung — không trả chi tiết nội bộ ra client (chỉ ghi log)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Object>> handleGlobalException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.internalServerError().body(ApiResult.fail("Đã có lỗi xảy ra"));
    }
}
