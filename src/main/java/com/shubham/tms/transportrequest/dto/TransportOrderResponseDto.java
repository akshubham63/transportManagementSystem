package com.shubham.tms.transportrequest.dto;

import com.shubham.tms.transportrequest.entity.TransportOrder;
import com.shubham.tms.transportrequest.enums.TransportOrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Builder
@Data
public class TransportOrderResponseDto {
    private UUID id;
    private UUID transportRequestId;
    private String requestNumber;
    private Date createdAt;
    private TransportOrderStatus transportOrderStatus;

    public static TransportOrderResponseDto fromEntity(TransportOrder transportOrder){
        return TransportOrderResponseDto.builder()
                .id(transportOrder.getId())
                .transportRequestId(transportOrder.getTransportRequest().getId())
                .requestNumber(transportOrder.getTransportRequest().getRequestNumber())
                .createdAt(transportOrder.getCreatedAt())
                .transportOrderStatus(transportOrder.getTransportOrderStatus())
                .build();
    }
}
