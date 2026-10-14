package com.shubham.tms.transportrequest.dto;

import com.shubham.tms.transportrequest.entity.TransportOrder;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Builder
@Data
public class TransportOrderResponse {
    private UUID id;
    private UUID transportRequestId;
    private String requestNumber;
    private Date createdAt;

    public static TransportOrderResponse fromEntity(TransportOrder transportOrder){
        return TransportOrderResponse.builder()
                .id(transportOrder.getId())
                .transportRequestId(transportOrder.getTransportRequest().getId())
                .requestNumber(transportOrder.getTransportRequest().getRequestNumber())
                .createdAt(transportOrder.getCreatedAt())
                .build();
    }
}
