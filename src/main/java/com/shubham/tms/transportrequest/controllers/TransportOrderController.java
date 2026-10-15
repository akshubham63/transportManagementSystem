package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.TransportOrderResponse;
import com.shubham.tms.transportrequest.entity.TransportOrder;
import com.shubham.tms.transportrequest.service.TransportOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<TransportOrderResponse>> getAllTransportOrders() {
        List<TransportOrderResponse> transportOrderResponseList = transportOrderService.getAllTransportOrders();
        return ResponseEntity.ok()
                .body(transportOrderResponseList);
    }
}
