package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.dto.GetApiResponseDto;
import com.shubham.tms.transportrequest.dto.TransportOrderStatusUpdateDto;
import com.shubham.tms.transportrequest.enums.SortDirection;
import com.shubham.tms.transportrequest.enums.TransportOrderStatus;
import com.shubham.tms.transportrequest.exceptions.TransportOrderNotFoundException;
import com.shubham.tms.transportrequest.dto.TransportOrderResponseDto;
import com.shubham.tms.transportrequest.entity.TransportOrder;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import com.shubham.tms.transportrequest.exceptions.TransportOrderAlreadyExists;
import com.shubham.tms.transportrequest.exceptions.TransportRequestNotFoundException;
import com.shubham.tms.transportrequest.exceptions.TransportException;
import com.shubham.tms.transportrequest.repository.TransportOrderRepository;
import com.shubham.tms.transportrequest.repository.TransportRequestRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map;

@Service
public class TransportOrderService {
    public final TransportOrderRepository transportOrderRepository;
    public final TransportRequestRepository transportRequestRepository;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "createdAt"
    );
    private static final Map<TransportOrderStatus, Set<TransportOrderStatus>> VALID_TRANSITION = Map.of(
            TransportOrderStatus.CREATED, Set.of(
                    TransportOrderStatus.ASSIGNED
            ),
            TransportOrderStatus.ASSIGNED, Set.of(
                    TransportOrderStatus.IN_TRANSIT
            ),
            TransportOrderStatus.IN_TRANSIT, Set.of(
                    TransportOrderStatus.COMPLETED
            )
    );

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
                .transportOrderStatus(TransportOrderStatus.CREATED)
                .build();
        try {
            return transportOrderRepository.saveAndFlush(transportOrder);
        } catch (DataIntegrityViolationException ex) {
            if(isTransportRequestUniqueConstraintViolation(ex)) {
                throw new TransportOrderAlreadyExists(
                        "A transport order already exists for transport request ID: "
                                + transportRequestId
                );
            }
            throw ex;
        }
    }

    public TransportOrderResponseDto getTransportOrderById(UUID transportOrderId) {
        TransportOrder transportOrder = transportOrderRepository.findById(transportOrderId)
                .orElseThrow(() -> new TransportOrderNotFoundException("Transport order with id: " + transportOrderId + " not found"));
        return TransportOrderResponseDto.fromEntity(transportOrder);
    }

    public GetApiResponseDto<TransportOrderResponseDto> getAllTransportOrders(
            int page, int limit, SortDirection sortDirection, String sortBy
    ) {
        String finalSortBy;
        if(sortBy == null) {
            finalSortBy = "createdAt";
        } else if (ALLOWED_SORT_FIELDS.contains(sortBy)) {
            finalSortBy = sortBy;
        } else {
            throw new IllegalArgumentException("Invalid sort field: '" + sortBy +
                    "'. Allowed fields are: " + ALLOWED_SORT_FIELDS);
        }
        Sort sort = Sort.by(
                sortDirection != null && sortDirection.equals(SortDirection.DESC)? Sort.Direction.DESC : Sort.Direction.ASC,
                finalSortBy
        );
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<TransportOrder> transportOrderList = transportOrderRepository.findAll(pageable);
        List<TransportOrderResponseDto> transportOrderResponseDtoList = transportOrderList.stream()
                .map(TransportOrderResponseDto::fromEntity)
                .toList();
        return GetApiResponseDto.<TransportOrderResponseDto>builder()
                .count(transportOrderResponseDtoList.size())
                .totalRecords(transportOrderList.getTotalElements())
                .data(transportOrderResponseDtoList)
                .build();
    }

    public TransportOrderResponseDto updateTransportOrderStatus(UUID transportOrderId, TransportOrderStatusUpdateDto transportOrderStatusUpdateDto) {
        if(!TransportOrderStatus.isValid(transportOrderStatusUpdateDto.getStatus().name())) {
            throw new TransportException("Invalid transport order status: " + transportOrderStatusUpdateDto.getStatus());
        }
        TransportOrder transportOrder = transportOrderRepository.findById(transportOrderId)
                .orElseThrow(() -> new TransportOrderNotFoundException("Transport order with id: " + transportOrderId + " not found"));

        TransportOrderStatus currentOrderStatus = transportOrder.getTransportOrderStatus();
        TransportOrderStatus newStatus = transportOrderStatusUpdateDto.getStatus();

        if(currentOrderStatus == newStatus) {
            throw new TransportException("Transport order is already in status: " + currentOrderStatus);
        }

        Set<TransportOrderStatus> allowedTargets = VALID_TRANSITION.getOrDefault(currentOrderStatus, Set.of());
        if(!allowedTargets.contains(newStatus)) {
            throw new TransportException("Cannot transition transport order from " + currentOrderStatus + " to " + newStatus);
        }

        transportOrder.setTransportOrderStatus(transportOrderStatusUpdateDto.getStatus());
        TransportOrder updatedTransportOrder = transportOrderRepository.save(transportOrder);
        return TransportOrderResponseDto.fromEntity(updatedTransportOrder);
    }

    private boolean isTransportRequestUniqueConstraintViolation(DataIntegrityViolationException ex) {
        Throwable cause = ex;

        while (cause != null) {
            if(cause instanceof org.hibernate.exception.ConstraintViolationException cve) {
                return "uk_transport_order_request".equals(cve.getConstraintName());
            }
            cause = cause.getCause();
        }

        return false;
    }
}
