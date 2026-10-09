package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.GetApiResponse;
import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.dto.TransportRequestResponseDto;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.service.TransportRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transport-requests")
public class TransportRequestController {
    public final TransportRequestService transportRequestService;

    public TransportRequestController(TransportRequestService transportRequestService){
        this.transportRequestService = transportRequestService;
    }

    @PostMapping
    public ResponseEntity<TransportRequestResponseDto> createTransportRequest(@Valid @RequestBody TransportRequestBodyDto transportRequestBodyDto){
        TransportRequestResponseDto transportRequest = transportRequestService.saveTransportRequest(transportRequestBodyDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transportRequest);
    }

    @GetMapping
    public ResponseEntity<GetApiResponse<TransportRequestResponseDto>> getAllTransportRequest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        GetApiResponse<TransportRequestResponseDto> allTransportRequest = transportRequestService.getAllTransportRequest(page, limit);
        return ResponseEntity.ok(allTransportRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportRequestResponseDto> getTransportRequestById(@PathVariable("id") UUID id) {
        TransportRequestResponseDto transportRequestById = transportRequestService.getTransportRequestById(id);
        return ResponseEntity.ok(transportRequestById);
    }
}
