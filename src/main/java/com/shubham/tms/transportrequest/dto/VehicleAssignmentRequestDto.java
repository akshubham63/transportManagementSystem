package com.shubham.tms.transportrequest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleAssignmentRequestDto {
    @NotBlank(message = "Vehicle name is mandatory")
    private String name;
}
