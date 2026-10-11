package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.GetApiResponse;
import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.dto.TransportRequestResponseDto;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.enums.SortDirection;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import com.shubham.tms.transportrequest.service.TransportRequestService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/transport-requests")
@Validated
public class TransportRequestController {
    public final TransportRequestService transportRequestService;

    public TransportRequestController(TransportRequestService transportRequestService){
        this.transportRequestService = transportRequestService;
    }

    @PostMapping
    public ResponseEntity<TransportRequestResponseDto> createTransportRequest(
            @Valid @RequestBody TransportRequestBodyDto transportRequestBodyDto,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ){
        TransportRequestResponseDto transportRequestResponseDto = transportRequestService.saveTransportRequest(transportRequestBodyDto, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transportRequestResponseDto);
    }

    @GetMapping
    public ResponseEntity<GetApiResponse<TransportRequestResponseDto>> getAllTransportRequest(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page index must be 0 or greater") Integer page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Limit must be 1 or greater") @Max(value = 100, message = "Limit must be less than or equal to 100") Integer limit,
            @RequestParam(required = false) TransportRequestStatus status,
            @RequestParam(required = false) @Min(value = 1, message = "Limit must be 1 or greater") @Max(value = 99, message = "Limit must be less than or equal to 99") Integer priority,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) Double quantity,
            @RequestParam(required = false) Date createdAt,
            @RequestParam(required = false) Date updatedAt,
            @RequestParam(required = false) SortDirection sortDirection,
            @RequestParam(required = false) String sortBy
            ) {
        GetApiResponse<TransportRequestResponseDto> allTransportRequest = transportRequestService.getAllTransportRequest(
                page, limit, status, priority, source, destination, material, quantity, updatedAt, createdAt, sortDirection, sortBy
        );
        return ResponseEntity.ok(allTransportRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportRequestResponseDto> getTransportRequestById(@PathVariable("id") UUID id) {
        TransportRequestResponseDto transportRequestResponseById = transportRequestService.getTransportRequestById(id);
        return ResponseEntity.ok(transportRequestResponseById);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportRequestResponseDto> updateTransportRequest(
            @PathVariable("id") UUID id,
            @Valid @RequestBody TransportRequestBodyDto transportRequestBodyDto) {
        TransportRequestResponseDto transportRequestResponseDto = transportRequestService.updateTransportRequestById(id, transportRequestBodyDto);
        return ResponseEntity.ok(transportRequestResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportRequest(
            @PathVariable("id") UUID id
    ) {
        transportRequestService.deleteTransportRequest(id);
        return ResponseEntity.noContent().build();
    }
}
