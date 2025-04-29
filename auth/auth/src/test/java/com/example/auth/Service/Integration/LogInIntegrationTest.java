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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogInIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    UserRepository userRepository;

    private static HttpHeaders headers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/auth/login";
    }

    @Test
    public void test_loginUser_success() throws JsonProcessingException {
        User user = new User();
        user.setCreatedAt(LocalDateTime.now());        user.setName("hello");
        user.setEmail("hello@gmail.com");
        user.setPassword("AWE895");

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
        ResponseEntity<CommonResponse> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.POST, entity, CommonResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals("Login successful!", response.getBody().getMessage());
     }

    @Test
    public void test_loginUser_failure() throws JsonProcessingException {
        User wrongUser = new User();
        wrongUser.setCreatedAt(LocalDateTime.now());
        wrongUser.setName("something");
        wrongUser.setEmail("something@gmail.com");
        wrongUser.setPassword("WRONG");

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(wrongUser), headers);

        ResponseEntity<CommonResponse> response ;
        try{
            response = restTemplate.exchange(
                    createURLWithPort(), HttpMethod.POST, entity, CommonResponse.class);

            System.out.println(response);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            assertEquals("Invalid credentials", response.getBody().getMessage());
        }
        catch (HttpClientErrorException e){
            System.out.println("Error " + e.getMessage());
        }
    }

}
