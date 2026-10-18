package com.shubham.tms.transportrequest.dto;

import com.shubham.tms.transportrequest.enums.FleetTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class VehicleRequestDto {
    @NotBlank(message = "Vehicle name is mandatory")
    private String name;

    @NotNull(message = "Fleet type must not be null")
    private FleetTypeEnum fleetTypeEnum;
}
