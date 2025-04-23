package com.example.auth.Service;

import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
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

@ExtendWith(MockitoExtension.class)             // throws NullPointerException
public class TestRegisterUser {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImple userService;

    @Test
    public void test_userRegister(){
        UserRequest request = new UserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("something");
        request.setName("user");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(Mockito.any(User.class));

        ResponseEntity<CommonResponse> response = userService.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(201, response.getBody().getStatusCode());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    public void test_EmailExists(){
        UserRequest request = new UserRequest();
        request.setEmail("hello@gmail.com");
        request.setPassword("something");
        request.setName("user");

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        ResponseEntity<CommonResponse> response = userService.register(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatusCode());
        assertEquals("Email already exists", response.getBody().getMessage());
    }


}
