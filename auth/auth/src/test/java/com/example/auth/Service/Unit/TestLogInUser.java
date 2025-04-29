package com.example.auth.Service.Unit;

import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
import com.example.auth.Service.UserService;
import com.example.auth.Service.UserServiceImple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)             // throws NullPointerException
public class TestLogInUser {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImple userService;

    @Test
    public void test_InvalidCredentials(){
        UserRequest request = new UserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("something");

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(Mockito.any(User.class)));

        ResponseEntity<CommonResponse> response = userService.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getStatusCode());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }

    @Test
    public void test_isLoginSuccess(){
        UserRequest request = new UserRequest();
        request.setEmail("hello@gmail.com");
        request.setPassword("AWE895");

        User user = new User();
        user.setEmail("hello@gmail.com");
        user.setPassword("AWE895");

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        ResponseEntity<CommonResponse> response = userService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("Login successful!", response.getBody().getMessage());
    }

    @Test
    public void test_isPasswordNotMatch(){
        UserRequest request = new UserRequest();
        request.setEmail("hello@gmail.com");
        request.setPassword("AWE777");

        User user = new User();
        user.setEmail("hello@gmail.com");
        user.setPassword("AWE895");

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        ResponseEntity<CommonResponse> response = userService.login(request);

        assertNotEquals(request.getPassword(),user.getPassword());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getStatusCode());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }
}

