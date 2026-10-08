package com.shubham.tms.transportrequest.entity;

import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportRequestStatus status = TransportRequestStatus.CREATED;

    @Builder.Default
    @CreatedDate
    @Column(nullable = false)
    private Date createdAt = new Date();

    @LastModifiedDate
    private Date updatedAt;

    @PrePersist
    public void generateRequestNumber() {
        // 1. Get the current date and time format down to the millisecond/second
        String timePart = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

        // 2. Generate a secure, 4-digit numeric random tail (0000 to 9999)
        // This reduces the mathematical probability of a clash during the exact same second to near zero.
        int randomTail = ThreadLocalRandom.current().nextInt(1000, 10000);

        this.requestNumber = "TR-" + timePart + "-" + randomTail;
        // Example Result: TR-20261008-204115-7482
    }

    @PostUpdate
    public void preUpdateActions() {
        this.updatedAt = new Date();
    }
}
