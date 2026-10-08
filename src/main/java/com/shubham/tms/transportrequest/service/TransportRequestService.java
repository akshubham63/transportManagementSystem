package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.repository.TransportRequestRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransportRequestService {
    public final TransportRequestRepository transportRequestRepository;

    public TransportRequestService(TransportRequestRepository transportRequestRepository) {
        this.transportRequestRepository = transportRequestRepository;
    }

    public TransportRequest saveTransportRequest(TransportRequestBodyDto transportRequestBodyDto) {
        TransportRequest transportRequest = TransportRequest.builder()
                .createdAt(new Date())
                .destination(transportRequestBodyDto.getDestination())
                .material(transportRequestBodyDto.getMaterial())
                .priority(transportRequestBodyDto.getPriority())
                .quantity(transportRequestBodyDto.getQuantity())
                .source(transportRequestBodyDto.getSource())
                .build();
        return transportRequestRepository.save(transportRequest);
    }
}
