package com.example.auth.Controller;

import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import com.example.auth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> register(@RequestBody UserRequest user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody UserRequest user) {
        return userService.login(user);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<CommonResponse> forgotPassword(@RequestBody UserRequest user) {
        return userService.forgotPassword(user);
    }

    @GetMapping("/available/{userId}")
    public Boolean isAvailableUser(@PathVariable Long userId ){
        return userService.isUserAvailable(userId);
    }
}
