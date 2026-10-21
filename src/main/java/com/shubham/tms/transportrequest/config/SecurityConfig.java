package com.shubham.tms.transportrequest.config;

import com.shubham.tms.transportrequest.security.JwtAuthenticationFilter;
import com.shubham.tms.transportrequest.service.CustomUserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/*
    1. After adding the dependency of spring-boot-security the spring boot application enables the basic authentication. The client will send the authorization in header ex. Authorization: Basic <encoded-string>.
    2. @EnableWebSecurity: This annotation signals spring to enable its web security support. This is what makes your application secured. It's used in conjunction with @Configuration.
    3. WebSecurityConfigureAdapter: It is a utility class in the spring security framework that provides default configurations and allows customization of certain features. By extending it, you can configure and customize spring security for your application needs. (In old version we used to extends this class in never version we can directly create bean filter).
*/

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public final CustomUserDetailsServiceImpl customUserDetailsService;
    public final UserDetailsService userDetailsService;
    public final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsServiceImpl customUserDetailsService,
            UserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // disabled CRSF protection for your REST APIs.
                .authorizeHttpRequests( // Authorize http requests
                        auth -> auth
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/transport-orders/**").authenticated()
                                .requestMatchers("/api/fleet-manager/**").authenticated()
                                .requestMatchers("/api/transport-requests/**").authenticated()
                                .requestMatchers("/api/user/**").authenticated()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build(); // builds the filter chain configuration
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
