package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.TransportOrderResponse;
import com.shubham.tms.transportrequest.entity.TransportOrder;
import com.shubham.tms.transportrequest.service.TransportOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/transport-requests")
public class TransportOrderController {
    public final TransportOrderService transportOrderService;

    public TransportOrderController(TransportOrderService transportOrderService) {
        this.transportOrderService = transportOrderService;
    }

    @PostMapping("/{id}/transport-order")
    public ResponseEntity<TransportOrderResponse> createTransportOrder(@PathVariable("id") UUID id) {
        TransportOrder transportOrder = transportOrderService.createTransportOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(TransportOrderResponse.fromEntity(transportOrder));
    }
}
