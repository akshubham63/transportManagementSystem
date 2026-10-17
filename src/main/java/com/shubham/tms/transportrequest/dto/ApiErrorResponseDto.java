package com.shubham.tms.transportrequest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timeStamp;
    int status;
    String error;
    String message;
    String path;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, String> fieldErrors;

    // Core constructor for standard exception responses
    public ApiErrorResponseDto(HttpStatus status, String message, String path) {
        this(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path, null);
    }

    // Secondary constructor tailored for multi-field validation maps
    public ApiErrorResponseDto(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        this(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors);
    }
}
