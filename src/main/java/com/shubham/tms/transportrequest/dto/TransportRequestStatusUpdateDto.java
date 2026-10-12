package com.shubham.tms.transportrequest.dto;

import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransportRequestStatusUpdateDto {
    @NotNull(message = "Transport request status must not be null")
    private TransportRequestStatus status;
}
