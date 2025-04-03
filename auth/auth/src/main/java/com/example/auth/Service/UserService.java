package com.example.auth.Service;

import com.example.auth.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserService {
    String register(User user);
    String login(User user);
    String forgotPassword(User user);
}
