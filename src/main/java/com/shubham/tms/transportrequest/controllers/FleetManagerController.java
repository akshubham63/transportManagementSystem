package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.GetApiResponseDto;
import com.shubham.tms.transportrequest.dto.VehicleRequestDto;
import com.shubham.tms.transportrequest.dto.VehicleResponseDto;
import com.shubham.tms.transportrequest.enums.SortDirection;
import com.shubham.tms.transportrequest.service.FleetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@RestController
@RequestMapping("/api/fleet-manager")
@Validated
public class FleetManagerController {
    public FleetService fleetService;

    public FleetManagerController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDto> createVehicle(
            @Valid @RequestBody VehicleRequestDto vehicleRequestDto
            ) {
        VehicleResponseDto fleet = fleetService.createFleet(vehicleRequestDto);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(fleet);
    }

    @GetMapping
    public ResponseEntity<GetApiResponseDto<VehicleResponseDto>> getAllVehicles(
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0, message = "Page index must be 0 or greater") int page,
            @RequestParam(required = false, defaultValue = "10")  @Min(value = 1, message = "Limit must be 1 or greater") @Max(value = 100, message = "Limit must be less than or equal to 100") int limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) SortDirection sortDirection
            ) {
        GetApiResponseDto<VehicleResponseDto> allFleets = fleetService.getAllFleets(page, limit, sortBy, sortDirection);
        return ResponseEntity.ok(allFleets);
    }
}
