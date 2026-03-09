package com.datn.moneyai.exceptions;

import com.datn.moneyai.models.global.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserMessageException.class)
    public ResponseEntity<ApiResult<Object>> handleUserMessageException(UserMessageException ex) {
        return ResponseEntity.badRequest().body(ApiResult.fail(ex.getMessage()));
    }

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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResult<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ApiResult.fail(ex.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResult<Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResult.fail("Không tìm thấy đường dẫn bạn yêu cầu (404)"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResult<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(405)
                .body(ApiResult.fail("Phương thức HTTP không được hỗ trợ cho API này"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Object>> handleGlobalException(Exception ex) {
        ApiResult<Object> error = new ApiResult<>();
        error.setStatus(false);
        error.setUserMessage("Đã có lỗi xảy ra");
        error.setInternalMessage(ex.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}
