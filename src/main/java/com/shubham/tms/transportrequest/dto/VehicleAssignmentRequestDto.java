package com.shubham.tms.transportrequest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleAssignmentRequestDto {
    @NotNull(message = "Vehicle name is mandatory")
    private String name;
}
