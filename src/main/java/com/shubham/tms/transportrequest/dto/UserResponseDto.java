package com.shubham.tms.transportrequest.dto;

import lombok.Data;
import lombok.Builder;
import com.shubham.tms.transportrequest.entity.User;

import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID id;
    private String email;
    private String username;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmailId())
                .username(user.getUsername())
                .build();
    }
}
