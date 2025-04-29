package com.example.auth.Service.WebMVC;

import com.example.auth.Controller.UserController;
import com.example.auth.Model.Request.UserRequest;
import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// spring application context will start
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//@Autowired
//private TestRestTemplate restTemplate;

//@AutoConfigureMockMvc                       // no need start application
@WebMvcTest(UserController.class)
public class TestRegisterUserAPI {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Test
    public void test_register_success() throws Exception {

        when(userService.register(any(UserRequest.class)))
                .thenReturn(ResponseEntity.ok(new CommonResponse(200, "User Added successful!", null)));


        String userJson = "{ \"email\": \"test@example.com\", \"password\": \"test123\" ,\"username\": \"hello\" }";

        this.mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());
    }

    @Test
    public void test_register_failure() throws Exception {

        String userJson = "{ \"email\": \"test@gmail.com\", \"password\": \"test123\" ,\"username\": \"hello\" }";

        this.mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict());
    }

}
