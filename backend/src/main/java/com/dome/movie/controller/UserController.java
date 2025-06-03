package com.dome.movie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dome.movie.service.UserService;
import com.dome.movie.dto.LoginRequest;
import com.dome.movie.model.User;
import com.dome.movie.repository.UserRepository;
import com.dome.movie.security.UserPrincipal;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
public class UserController {
    private UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> newUser(@RequestBody() User user){
        User newUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            UserPrincipal userDetails = new UserPrincipal(user);
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
            request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", new SecurityContextImpl(authToken));

            return ResponseEntity.ok(Map.of("userId", user.getId(), "username", user.getUsername()));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
