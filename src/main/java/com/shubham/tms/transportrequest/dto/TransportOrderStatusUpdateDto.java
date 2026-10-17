package com.shubham.tms.transportrequest.dto;

import com.shubham.tms.transportrequest.enums.TransportOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransportOrderStatusUpdateDto {
    @NotNull(message = "Transport order status must not be null")
    private TransportOrderStatus status;
}
