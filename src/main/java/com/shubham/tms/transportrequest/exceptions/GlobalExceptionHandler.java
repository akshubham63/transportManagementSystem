package com.shubham.tms.transportrequest.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.shubham.tms.transportrequest.dto.ApiErrorResponse;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorResponse errorResponse = new ApiErrorResponse(status, "Validation failed", request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(TransportRequestNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTransportResponseNotFound(TransportRequestNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(status, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(java.lang.IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(java.lang.IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(status, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(status, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(TransportRequestStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleTransportRequestStatusException(TransportRequestStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(status, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(DuplicateIdempotencyKeyException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation (DuplicateIdempotencyKeyException ex, HttpServletRequest request) {
        String message = "A conflict occurred. This request may have already been processed.";
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(status, message, request.getRequestURI()));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = isInvalidTransportRequestStatus(ex)
                ? "Invalid transport request status"
                : "Malformed JSON request body";

        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(status, message, request.getRequestURI()));
    }

    private boolean isInvalidTransportRequestStatus(Throwable ex) {
        Throwable cause = ex;

        while (cause != null) {
            if (cause instanceof InvalidFormatException invalidFormatException) {
                return invalidFormatException.getTargetType()
                        .equals(TransportRequestStatus.class);
            }

            cause = cause.getCause();
        }

        return false;
    }
}
