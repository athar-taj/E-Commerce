package com.example.auth.Service;

import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImple implements UserService{
    @Autowired
    private UserRepository userRepository;


    public String register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists";
        }
        user.setPassword(user.getPassword());
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            User user1 = userOptional.get();
            if (user1.getPassword().equals(user.getPassword())) {
                return "Login successful!";
            }
        }
        return "Invalid credentials";
    }

    public String forgotPassword(User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            userOptional.get().setPassword(user.getPassword());
            userRepository.save(userOptional.get());
            return "Password Updated for " + user.getEmail();
        }
        return "Email not found";
    }
}
