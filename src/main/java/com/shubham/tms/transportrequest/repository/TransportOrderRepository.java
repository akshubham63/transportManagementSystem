package com.shubham.tms.transportrequest.repository;

import com.shubham.tms.transportrequest.entity.TransportOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TransportOrderRepository extends JpaRepository<TransportOrder, UUID>, JpaSpecificationExecutor<TransportOrder> {
    boolean existsByTransportRequestId(UUID transportRequestId);
}
