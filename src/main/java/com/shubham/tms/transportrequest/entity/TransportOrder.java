package com.shubham.tms.transportrequest.entity;

import com.shubham.tms.transportrequest.enums.TransportOrderStatus;
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
                columnNames = {"transport_request_id", "fleet_id"}
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportOrderStatus transportOrderStatus;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    private Date updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fleet_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_transport_order_fleet")
    )
    private Vehicle vehicle;

    @PrePersist
    protected void onCreated() {
        setTransportOrderStatus(TransportOrderStatus.CREATED);
        setCreatedAt(new Date());
    }

    @PreUpdate
    protected void onUpdated() {
        setUpdatedAt(new Date());
    }
}
