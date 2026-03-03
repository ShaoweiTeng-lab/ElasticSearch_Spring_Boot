package org.example.elasticsearch.exception;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import lombok.extern.slf4j.Slf4j;
import org.example.elasticsearch.common.response.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // 自訂業務錯誤
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultResponse<?>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.ok(ResultResponse.fail(ex.getCode(), ex.getMessage()));
    }

    // 參數驗證錯誤 (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = Objects.requireNonNull(ex.getBindingResult()
                        .getFieldError())
                .getDefaultMessage();

        return ResponseEntity.ok(ResultResponse.fail(400, message));
    }

    // 參數類型錯誤
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResultResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.ok(ResultResponse.fail(400, "參數類型錯誤"));
    }

    // Elasticsearch 錯誤
    @ExceptionHandler(ElasticsearchException.class)
    public ResponseEntity<ResultResponse<?>> handleESException(Exception ex) {
        log.warn("搜尋系統錯誤 : {}",ex.getMessage());
        return ResponseEntity.ok(ResultResponse.fail(500, "搜尋系統錯誤"));
    }

    // 其他未知錯誤
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<?>> handleException(Exception ex) {
        log.error("發生異常 ： {}", ex.getMessage()); // 可改成 log.error
        return ResponseEntity.ok(ResultResponse.fail(500, "系統發生錯誤"));
    }
}
