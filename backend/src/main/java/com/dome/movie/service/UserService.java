package com.dome.movie.service;

import com.dome.movie.model.User;
import com.dome.movie.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        if(!user.getUsername().equals(username)){
            throw new UsernameNotFoundException("User does not exist in the database");
        }

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw  new BadCredentialsException("The password is incorrect");
        }

        return  true;
    }

    public ResponseEntity<?> getAuthenticatedUser(UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(user);
    }
}