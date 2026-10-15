package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.GetApiResponse;
import com.shubham.tms.transportrequest.dto.TransportOrderResponse;
import com.shubham.tms.transportrequest.entity.TransportOrder;
import com.shubham.tms.transportrequest.enums.SortDirection;
import com.shubham.tms.transportrequest.service.TransportOrderService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import java.util.UUID;

@RestController
public class TransportOrderController {
    public final TransportOrderService transportOrderService;

    public TransportOrderController(TransportOrderService transportOrderService) {
        this.transportOrderService = transportOrderService;
    }

    @PostMapping("/api/transport-requests/{id}/transport-order")
    public ResponseEntity<TransportOrderResponse> createTransportOrder(@PathVariable("id") UUID id) {
        TransportOrder transportOrder = transportOrderService.createTransportOrder(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransportOrderResponse.fromEntity(transportOrder));
    }

    @GetMapping("/api/transport-orders/{id}")
    public ResponseEntity<TransportOrderResponse> getTransportOrderById(@PathVariable("id") UUID id) {
        TransportOrderResponse transportOrderResponse = transportOrderService.getTransportOrderById(id);
        return ResponseEntity.ok()
                .body(transportOrderResponse);
    }

    @GetMapping("/api/transport-orders")
    public ResponseEntity<GetApiResponse<TransportOrderResponse>> getAllTransportOrders(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "Page index must be 0 or greater") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 1, message = "Limit must be 1 or greater") @Max(value = 100, message = "Limit must be less than or equal to 100") int limit,
            @RequestParam(required = false) SortDirection sortDirection,
            @RequestParam(required = false) String sortBy
            ) {
        GetApiResponse<TransportOrderResponse> transportOrderResponseList = transportOrderService.getAllTransportOrders(page, limit, sortDirection, sortBy);
        return ResponseEntity.ok()
                .body(transportOrderResponseList);
    }
}
