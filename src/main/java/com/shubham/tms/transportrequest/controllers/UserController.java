package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.UserRequestDto;
import com.shubham.tms.transportrequest.dto.UserResponseDto;
import com.shubham.tms.transportrequest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/user")
@Validated
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("username") String userName) {
        UserResponseDto user = userService.getUserByUserName(userName);
        return ResponseEntity.ok().body(user);
    }
}
