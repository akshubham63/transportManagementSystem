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
            @RequestParam int page,
            @RequestParam int limit,
            @RequestParam String sortBy,
            @RequestParam SortDirection sortDirection
            ) {
        GetApiResponseDto<VehicleResponseDto> allFleets = fleetService.getAllFleets(page, limit, sortBy, sortDirection);
        return ResponseEntity.ok(allFleets);
    }
}
