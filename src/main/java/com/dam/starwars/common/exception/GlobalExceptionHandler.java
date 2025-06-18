package com.dam.starwars.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FilmNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFilmNotFound(FilmNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Filme Não Encontrado",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DateParseException.class)
    public ResponseEntity<Map<String, Object>> handleDateParseError(DateParseException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Erro de Formato de Data",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> error = createBaseErrorMap(HttpStatus.BAD_REQUEST, "Falha de Validação");
        error.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> errorMap = createBaseErrorMap(status, error);
        errorMap.put("message", message);
        return ResponseEntity.status(status).body(errorMap);
    }

    private Map<String, Object> createBaseErrorMap(HttpStatus status, String error) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", LocalDateTime.now());
        errorMap.put("status", status.value());
        errorMap.put("error", error);
        return errorMap;
    }
}