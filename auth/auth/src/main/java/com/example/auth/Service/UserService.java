package com.example.auth.Service;

import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

public interface UserService {
    ResponseEntity<CommonResponse> register(UserRequest user);
    ResponseEntity<CommonResponse> login(UserRequest user);
    ResponseEntity<CommonResponse> forgotPassword(UserRequest user,String authHeader);
    ResponseEntity<CommonResponse> validateToken(String authHeader);
    boolean isUserAvailable(Long userId);

}
