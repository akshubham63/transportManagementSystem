package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.entity.User;
import com.shubham.tms.transportrequest.exceptions.UserNotFoundException;
import com.shubham.tms.transportrequest.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    public final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User with username: " + username + " not exists");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
