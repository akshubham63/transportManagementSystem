package com.shubham.tms.transportrequest.enums;

import java.util.Arrays;

public enum TransportOrderStatus {
    CREATED,
    ASSIGNED,
    IN_TRANSIT,
    COMPLETED,
    REJECTED;

    public static boolean isValid(String value) {
        if(value == null) {
            return false;
        }
        return Arrays.stream(TransportOrderStatus.values())
                .anyMatch(status -> status.name().equals(value));
    }
}
