package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.dto.GetApiResponse;
import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.dto.TransportRequestResponseDto;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import com.shubham.tms.transportrequest.exceptions.TransportRequestNotFoundException;
import com.shubham.tms.transportrequest.exceptions.TransportRequestStatusException;
import com.shubham.tms.transportrequest.repository.TransportRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransportRequestService {
    public final TransportRequestRepository transportRequestRepository;

    public TransportRequestService(TransportRequestRepository transportRequestRepository) {
        this.transportRequestRepository = transportRequestRepository;
    }

    public TransportRequestResponseDto saveTransportRequest(TransportRequestBodyDto transportRequestBodyDto) {
        TransportRequest transportRequest = TransportRequest.builder()
                .destination(transportRequestBodyDto.getDestination())
                .material(transportRequestBodyDto.getMaterial())
                .priority(transportRequestBodyDto.getPriority())
                .quantity(transportRequestBodyDto.getQuantity())
                .source(transportRequestBodyDto.getSource())
                .build();
        return TransportRequestResponseDto.fromEntity(transportRequestRepository.save(transportRequest));
    }

    public GetApiResponse<TransportRequestResponseDto> getAllTransportRequest(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<TransportRequest> transportRequestList = transportRequestRepository.findAll(pageable);
        List<TransportRequestResponseDto> transportRequestResponseDtoList = transportRequestList.stream()
                .map(TransportRequestResponseDto::fromEntity)
                .toList();
        return GetApiResponse.<TransportRequestResponseDto>builder()
                    .count(transportRequestResponseDtoList.size())
                    .totalRecords(transportRequestList.getTotalElements())
                    .data(transportRequestResponseDtoList)
                    .build();
    }

    public TransportRequestResponseDto getTransportRequestById(UUID id) {
        TransportRequest transportRequest = transportRequestRepository.findById(id)
                .orElseThrow(() -> new TransportRequestNotFoundException("Transport request with id: " + id + " not found"));
        return TransportRequestResponseDto.fromEntity(transportRequest);
    }

    public TransportRequestResponseDto updateTransportRequestById(UUID id, TransportRequestBodyDto transportRequestBodyDto) {
        TransportRequest transportRequest = transportRequestRepository.findById(id)
                .orElseThrow(() -> new TransportRequestNotFoundException("Transport request with id: " + id + " not found"));
        if(!transportRequest.getStatus().equals(TransportRequestStatus.CREATED)) {
            throw new TransportRequestStatusException("Transport request with id: " + id + " is under process and cannot be updated!!!");
        }
        transportRequest.setDestination(transportRequestBodyDto.getDestination());
        transportRequest.setPriority(transportRequestBodyDto.getPriority());
        transportRequest.setMaterial(transportRequestBodyDto.getMaterial());
        transportRequest.setQuantity(transportRequestBodyDto.getQuantity());
        transportRequest.setSource(transportRequestBodyDto.getSource());

        TransportRequest updatedTransportRequest = transportRequestRepository.save(transportRequest);
        return TransportRequestResponseDto.fromEntity(updatedTransportRequest);
    }
}
