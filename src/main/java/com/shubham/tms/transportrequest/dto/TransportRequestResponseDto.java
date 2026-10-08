package com.shubham.tms.transportrequest.dto;

import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class TransportRequestResponseDto {
    private UUID id;
    private String requestNumber;

    private String source;

    private String destination;

    private String material;

    private Double quantity;

    private Integer priority;

    private TransportRequestStatus status;

    private Date createdAt = new Date();

    private Date updatedAt;

    public static TransportRequestResponseDto fromEntity(TransportRequest transportRequest){
        return TransportRequestResponseDto.builder()
                .id(transportRequest.getId())
                .requestNumber(transportRequest.getRequestNumber())
                .source(transportRequest.getSource())
                .destination(transportRequest.getDestination())
                .material(transportRequest.getMaterial())
                .quantity(transportRequest.getQuantity())
                .priority(transportRequest.getPriority())
                .status(transportRequest.getStatus())
                .createdAt(transportRequest.getCreatedAt())
                .updatedAt(transportRequest.getUpdatedAt())
                .build();
    }

}
