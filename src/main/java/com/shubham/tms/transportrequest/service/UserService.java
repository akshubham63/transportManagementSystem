package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.dto.UserRequestDto;
import com.shubham.tms.transportrequest.dto.UserResponseDto;
import com.shubham.tms.transportrequest.exceptions.UserNotFoundException;
import com.shubham.tms.transportrequest.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.shubham.tms.transportrequest.entity.User;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String encryptedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        User user = User.builder()
                .emailId(userRequestDto.getEmailId())
                .username(userRequestDto.getUsername())
                .password(encryptedPassword)
                .build();
        return UserResponseDto.fromEntity(userRepository.save(user));
    }

    public void deleteUser(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User with username: " + userName + " not exists");
        }
        userRepository.delete(user.get());
    }

    public UserResponseDto getUserByUserName(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User with username: " + userName + " not exists");
        }
        return UserResponseDto.fromEntity(user.get());
    }
}
