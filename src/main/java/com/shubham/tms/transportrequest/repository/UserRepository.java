package com.shubham.tms.transportrequest.repository;

import com.shubham.tms.transportrequest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>{
    Optional<User> findByUsername(String username);
}
