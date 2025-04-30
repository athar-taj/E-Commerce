package com.example.auth.Service.WebMVC;

import com.example.auth.Controller.UserController;
import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class TestForgotPasswordAPI {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxsb0BnbWFpbC5jb20iLCJpYXQiOjE3NDYwMDk1MzYsImV4cCI6MTc0NjAwOTgzNn0.liZ--JryFroFc7KR0isXT61CcbQZsuGMOc9srGCfw8s";


    @Test
    public void test_forgotPassword_success() throws Exception {
        when(userService.forgotPassword(any(UserRequest.class),token))
                .thenReturn(ResponseEntity.ok(new CommonResponse(200, "Password Changed successful!", null)));

        String userJson = "{ \"email\": \"test@example.com\", \"password\": \"test123\" ,\"username\": \"hello\" }";

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());
    }

    @Test
    public void test_forgotPassword_failure() throws Exception {
        when(userService.forgotPassword(any(UserRequest.class),token))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse(404,"Email not found!!",null)));

        String userJson = "{ \"email\": \"testaa@example.com\", \"password\": \"test123\" ,\"username\": \"hello\" }";

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isNotFound());
    }

}
