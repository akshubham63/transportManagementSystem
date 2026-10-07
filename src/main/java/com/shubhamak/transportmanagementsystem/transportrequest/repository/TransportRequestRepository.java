package com.shubhamak.transportmanagementsystem.transportrequest.repository;

import com.shubhamak.transportmanagementsystem.transportrequest.entity.TransportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransportRequestRepository extends JpaRepository<TransportRequest, UUID> {
}
