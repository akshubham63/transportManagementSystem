package com.shubham.tms.transportrequest.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserRequestDto {
    @NotBlank(message = "Email can not be empty")
    @Size(max = 64, min = 4, message = "Email should have character in range of 4 to 64")
    private String emailId;

    @NotBlank(message = "Username can not be empty")
    @Size(min = 3, max = 64, message = "Username should have character in range of 3 to 64")
    private String username;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 4, max = 64, message = "Password should have character in range of 4 to 64")
    private String password;
}
