package com.shubham.tms.transportrequest.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
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
    public record ErrorResponse(String message) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(TransportRequestNotFoundException.class)
    public ResponseEntity<?> handleTransportResponseNotFound(TransportRequestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(java.lang.IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(java.lang.IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(TransportRequestStatusException.class)
    public ResponseEntity<ErrorResponse> handleTransportRequestStatusException(TransportRequestStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateIdempotencyKeyException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation (DuplicateIdempotencyKeyException ex) {
        String message = "A conflict occurred. This request may have already been processed.";
        ErrorResponse error = new ErrorResponse(message);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        if (isInvalidTransportRequestStatus(ex)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid transport request status")
            );
        }

        return ResponseEntity.badRequest().body(
                new ErrorResponse("Malformed JSON request body")
        );
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
