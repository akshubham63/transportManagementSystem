package com.shubham.tms.transportrequest.controllers;

import com.shubham.tms.transportrequest.dto.AuthRequestDto;
import com.shubham.tms.transportrequest.dto.AuthResponseDto;
import com.shubham.tms.transportrequest.dto.UserRequestDto;
import com.shubham.tms.transportrequest.dto.UserResponseDto;
import com.shubham.tms.transportrequest.service.JwtService;
import com.shubham.tms.transportrequest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/public")
public class PublicController {
    public final UserService userService;
    public final AuthenticationManager authenticationManager;
    public final UserDetailsService userDetailsService;
    public final JwtService jwtService;

    public PublicController(
            UserService userService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto user = userService.createUser(userRequestDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getUsername(),
                        authRequestDto.getPassword()
                )
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDto.getUsername());
        final String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponseDto(jwtToken));
    }
}
