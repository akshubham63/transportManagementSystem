package com.shubham.tms.transportrequest.entity;

import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
public class TransportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Size(max = 64)
    private String requestNumber;

    @Column(nullable = false)
    @Size(max = 64)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    @Max(99)
    @Min(1)
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportRequestStatus status = TransportRequestStatus.CREATED;

    @CreatedDate
    @Column(nullable = false)
    private Date createdAt = new Date();

    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;
}
