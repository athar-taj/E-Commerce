package com.example.auth.Service.Unit;

import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
import com.example.auth.Service.UserServiceImple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TestForgotPassword {

    @Mock
    UserRepository userRepository;
    @InjectMocks    // add mock objects to the Spring application context for testing purposes
    UserServiceImple userService;

    @Test
    public void test_emailExists(){
        UserRequest request = new UserRequest();
        request.setPassword("AWE895");
        request.setEmail("hello@gmail.com");

        User user = new User();
        user.setPassword("AWE895");
        user.setEmail("hello@gmail.com");

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxsb0BnbWFpbC5jb20iLCJpYXQiOjE3NDYwMDk1MzYsImV4cCI6MTc0NjAwOTgzNn0.liZ--JryFroFc7KR0isXT61CcbQZsuGMOc9srGCfw8s";
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        ResponseEntity<CommonResponse> response = userService.forgotPassword(request,token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("Password updated for " + user.getEmail(), response.getBody().getMessage());

    }
    @Test
    public void test_emailNotFound(){
        UserRequest request = new UserRequest();
        request.setPassword("AWE895");
        request.setEmail("hello1@gmail.com");

        User user = new User();
        user.setPassword("AWE895");
        user.setEmail("hello1@gmail.com");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxsb0BnbWFpbC5jb20iLCJpYXQiOjE3NDYwMDk1MzYsImV4cCI6MTc0NjAwOTgzNn0.liZ--JryFroFc7KR0isXT61CcbQZsuGMOc9srGCfw8s";

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<CommonResponse> response = userService.forgotPassword(request,token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatusCode());
        assertEquals("Email not found!!", response.getBody().getMessage());
    }
}
