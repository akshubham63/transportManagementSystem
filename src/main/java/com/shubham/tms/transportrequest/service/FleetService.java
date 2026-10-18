package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.dto.GetApiResponseDto;
import com.shubham.tms.transportrequest.dto.VehicleRequestDto;
import com.shubham.tms.transportrequest.dto.VehicleResponseDto;
import com.shubham.tms.transportrequest.entity.Vehicle;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.shubham.tms.transportrequest.repository.FleetRepository;
import com.shubham.tms.transportrequest.enums.SortDirection;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
public class FleetService {
    public final FleetRepository fleetRepository;

    public FleetService(
            FleetRepository fleetRepository
    ) {
        this.fleetRepository = fleetRepository;
    }

    public VehicleResponseDto createFleet(VehicleRequestDto vehicleRequestDto) {
        Vehicle vehicle = Vehicle.builder()
                .name(vehicleRequestDto.getName())
                .fleetTypeEnum(vehicleRequestDto.getFleetTypeEnum())
                .build();
        Vehicle savedVehicle = fleetRepository.save(vehicle);
        return VehicleResponseDto.fromEntity(savedVehicle);
    }

    public GetApiResponseDto<VehicleResponseDto> getAllFleets(
            int page, int limit, String sortBy, SortDirection sortDirection) {
        Sort sort = Sort.by(
                sortDirection != null && sortDirection.equals(SortDirection.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy
        );
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Vehicle> vehicles = fleetRepository.findAll(pageable);
        List<VehicleResponseDto> vehicleResponseDtoList = vehicles.stream()
                .map(VehicleResponseDto::fromEntity)
                .toList();
        return GetApiResponseDto.<VehicleResponseDto>builder()
                .data(vehicleResponseDtoList)
                .count(vehicleResponseDtoList.size())
                .totalRecords(vehicles.getTotalElements())
                .build();
    }
}
