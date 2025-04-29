package com.example.auth.Service.Integration;

import com.example.auth.Model.Response.CommonResponse;
import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegisterIntegrationTest {

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
        return "http://localhost:" + port + "/api/auth/signup";
    }

    @Test
    public void test_registerUser_success() throws JsonProcessingException {
        User user = new User();
        user.setName("het");
        user.setEmail("het@gmail.com");
        user.setPassword("AWG22");

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user),headers);
        ResponseEntity<CommonResponse> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.POST,entity,CommonResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    public void test_registerUser_failure() throws JsonProcessingException {
        User user = new User();
        user.setName("nothing");
        user.setEmail("something@gmail.com");
        user.setPassword("AWG22");

        try {
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of((new User())));

            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user),headers);
            ResponseEntity<CommonResponse> response = restTemplate.exchange(
                    createURLWithPort(), HttpMethod.POST,entity,CommonResponse.class);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            assertEquals("Email already exists", response.getBody().getMessage());
        }
        catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }
}
