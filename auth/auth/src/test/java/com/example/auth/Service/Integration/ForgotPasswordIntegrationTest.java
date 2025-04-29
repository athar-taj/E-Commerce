package com.example.auth.Service.Integration;

import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForgotPasswordIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;
    @MockitoBean
    private UserRepository userRepository;

    private static HttpHeaders headers;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeAll
    public static void init() {
        headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/auth/forgot-password";
    }

    @Test
    public void test_forgotPassword_success() {
        User user = new User();
        user.setName("het");
        user.setEmail("het@gmail.com");
        user.setPassword("XZS25");

        try {
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user),headers);
            ResponseEntity<CommonResponse> response= restTemplate.exchange(
                                                createURLWithPort(), HttpMethod.POST , entity, CommonResponse.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            assertEquals("Password updated for " + user.getEmail(), response.getBody().getMessage());
        }
        catch (Exception e){
            System.out.println("Exception :- " + e.getMessage());
        }
    }

    @Test
    public void test_forgotPassword_failure(){
        User user = new User();
        user.setName("het");
        user.setEmail("het1@gmail.com");
        user.setPassword("XZS25");

        try {
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user),headers);
            ResponseEntity<CommonResponse> response= restTemplate.exchange(
                    createURLWithPort(), HttpMethod.POST , entity, CommonResponse.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            assertEquals("Email not found!!", response.getBody().getMessage());
        }
        catch (Exception e){
            System.out.println("Exception :- " + e.getMessage());
        }
    }
}
