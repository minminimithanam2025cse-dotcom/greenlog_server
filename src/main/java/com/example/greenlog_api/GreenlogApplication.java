package com.example.greenlog_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example.repo")
@EntityScan(basePackages = "com.example.model")
public class GreenlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenlogApplication.class, args);
    }

    @RestControllerAdvice
    static class ApiExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        ResponseEntity<Map<String, Object>> handleRuntimeException(
                RuntimeException exception) {

            return buildResponse(
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage()
            );
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        ResponseEntity<Map<String, Object>> handleValidationException(
                MethodArgumentNotValidException exception) {

            Map<String, Object> errors = new LinkedHashMap<>();

            exception.getBindingResult()
                    .getFieldErrors()
                    .forEach(error ->
                            errors.putIfAbsent(
                                    error.getField(),
                                    error.getDefaultMessage()
                            )
                    );

            Map<String, Object> body = new LinkedHashMap<>();

            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Validation failed");
            body.put("messages", errors);

            return ResponseEntity
                    .badRequest()
                    .body(body);
        }

        private ResponseEntity<Map<String, Object>> buildResponse(
                HttpStatus status,
                String message) {

            Map<String, Object> body = new LinkedHashMap<>();

            body.put("timestamp", LocalDateTime.now());
            body.put("status", status.value());
            body.put("error", status.getReasonPhrase());
            body.put(
                    "message",
                    message == null ? "Request failed" : message
            );

            return ResponseEntity
                    .status(status)
                    .body(body);
        }
    }
}