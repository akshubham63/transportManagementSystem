package com.shubham.tms.transportrequest.dto;

import java.util.UUID;

import com.shubham.tms.transportrequest.entity.Vehicle;
import com.shubham.tms.transportrequest.enums.FleetTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDto {
    private UUID id;
    private String name;
    private FleetTypeEnum fleetTypeEnum;

    public static VehicleResponseDto fromEntity(Vehicle vehicle) {
        return VehicleResponseDto.builder()
                .id(vehicle.getId())
                .name(vehicle.getName())
                .fleetTypeEnum(vehicle.getFleetTypeEnum())
                .build();
    }
}
