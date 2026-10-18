package com.shubham.tms.transportrequest.repository;

import com.shubham.tms.transportrequest.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface FleetRespository extends JpaRepository<Vehicle, UUID>{
    public Optional<Vehicle> findByName(String name);
}
