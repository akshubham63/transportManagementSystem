package com.shubham.tms.transportrequest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    private String emailId;

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 64)
    private String username;

    @NotNull
    @Size(min = 4, max = 64)
    private String password;
}
