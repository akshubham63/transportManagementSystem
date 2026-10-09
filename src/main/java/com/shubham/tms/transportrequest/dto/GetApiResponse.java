package com.shubham.tms.transportrequest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetApiResponse<T> {
    private List<T> data;
    private Long totalRecords;
    private int count;
}
