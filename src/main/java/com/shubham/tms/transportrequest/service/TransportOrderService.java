package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.entity.TransportOrder;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import com.shubham.tms.transportrequest.exceptions.TransportOrderAlreadyExists;
import com.shubham.tms.transportrequest.exceptions.TransportRequestNotFoundException;
import com.shubham.tms.transportrequest.exceptions.TransportException;
import com.shubham.tms.transportrequest.repository.TransportOrderRepository;
import com.shubham.tms.transportrequest.repository.TransportRequestRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransportOrderService {
    public final TransportOrderRepository transportOrderRepository;
    public final TransportRequestRepository transportRequestRepository;

    public TransportOrderService(
            TransportOrderRepository transportOrderRepository,
            TransportRequestRepository transportRequestRepository
    ) {
        this.transportOrderRepository = transportOrderRepository;
        this.transportRequestRepository = transportRequestRepository;
    }

    public TransportOrder createTransportOrder(UUID transportRequestId) {
        if(transportOrderRepository.existsByTransportRequestId(transportRequestId)) {
            throw new TransportOrderAlreadyExists("A transport order already exists for transport request ID: " + transportRequestId);
        }
        TransportRequest transportRequest = transportRequestRepository.findById(transportRequestId)
                .orElseThrow(() -> new TransportRequestNotFoundException("Transport request with id: " + transportRequestId + " not found"));
        if(transportRequest.getStatus() != TransportRequestStatus.APPROVED) {
            throw new TransportException("Cannot create transport order. Transport request with ID " +transportRequestId + " is currently in " + transportRequest.getStatus() + " status, but must be APPROVED.");
        }

        TransportOrder transportOrder = TransportOrder.builder()
                .transportRequest(transportRequest)
                .build();

        return transportOrderRepository.save(transportOrder);
    }
}
