package com.shubham.tms.transportrequest.repository;

import com.shubham.tms.transportrequest.entity.TransportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TransportRequestRepository extends JpaRepository<TransportRequest, UUID>, JpaSpecificationExecutor<TransportRequest> {
    Boolean existsByIdempotencyKey(String key);
}
