package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.GetApiResponse;
import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.dto.TransportRequestResponseDto;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.service.TransportRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
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
    public ResponseEntity<TransportRequestResponseDto> createTransportRequest(@Valid @RequestBody TransportRequestBodyDto transportRequestBodyDto){
        TransportRequestResponseDto transportRequestResponseDto = transportRequestService.saveTransportRequest(transportRequestBodyDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transportRequestResponseDto);
    }

    @GetMapping
    public ResponseEntity<GetApiResponse<TransportRequestResponseDto>> getAllTransportRequest(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page index must be 0 or greater") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Limit must be 1 or greater") @Max(value = 100, message = "Limit must be less than or equal to 100") int limit
    ) {
        GetApiResponse<TransportRequestResponseDto> allTransportRequest = transportRequestService.getAllTransportRequest(page, limit);
        return ResponseEntity.ok(allTransportRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportRequestResponseDto> getTransportRequestById(@PathVariable("id") UUID id) {
        TransportRequestResponseDto transportRequestResponseById = transportRequestService.getTransportRequestById(id);
        return ResponseEntity.ok(transportRequestResponseById);
    }
}
