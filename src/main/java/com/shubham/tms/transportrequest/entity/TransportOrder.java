package com.shubham.tms.transportrequest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_transport_order_request",
                columnNames = "transport_request_id"
        )
)
public class TransportOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "transport_request_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transport_order_request")
    )
    private TransportRequest transportRequest;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreated() {
        setCreatedAt(new Date());
    }
}
