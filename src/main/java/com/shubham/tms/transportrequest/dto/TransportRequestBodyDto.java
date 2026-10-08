package com.shubham.tms.transportrequest.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransportRequestBodyDto {

    @NotBlank(message = "Source cannot be blank")
    private String source;

    @NotBlank(message = "Destination cannot be blank")
    private String destination;

    @NotBlank(message = "Material cannot be blank")
    private String material;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Double quantity;

    @NotNull(message = "Priority number cannot be blank")
    @Max(value = 99, message = "Priority of transport request cannot be greater than 99")
    @Min(value = 1, message = "Priority of transport request cannot be less than 1")
    private Integer priority;
}
