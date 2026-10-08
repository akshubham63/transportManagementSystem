package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.dto.TransportRequestResponseDto;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.service.TransportRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/transport-requests")
public class TransportRequestController {
    public final TransportRequestService transportRequestService;

    public TransportRequestController(TransportRequestService transportRequestService){
        this.transportRequestService = transportRequestService;
    }

    @PostMapping
    public ResponseEntity<TransportRequestResponseDto> createTransportRequest(@Valid @RequestBody TransportRequestBodyDto transportRequestBodyDto){
        TransportRequest transportRequest = transportRequestService.saveTransportRequest(transportRequestBodyDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransportRequestResponseDto.fromEntity(transportRequest));
    }
}
