package com.example.auth.Service;

import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImple implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<CommonResponse> register(@Valid UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResponse(409, "Email already exists", null));
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setActive(Boolean.TRUE);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "User registered successfully", user));
    }


    public ResponseEntity<CommonResponse> login(@Valid UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(userRequest.getPassword())) {
            return  ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Login successful!", userOptional.get()));
        }
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse(401, "Invalid credentials", null));
    }

    public ResponseEntity<CommonResponse> forgotPassword(@Valid UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userRepository.save(user);
            return  ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Password updated for " + user.getEmail(), user));
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse(404, "Email not found!!", null));
    }
    @Override
    public boolean isUserAvailable(Long userId) {
        return userRepository.existsById(userId);
    }
}