package com.example.auth.Service.Data;


import com.example.auth.Model.User;
import com.example.auth.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)     // Default Using H2 DB
public class TestLogInData {
    @Autowired
    UserRepository userRepository;

    @Test
    public void test_UerLogIn(){
        User user = new User();
        user.setName("Hello");
        user.setEmail("hello@gmail.com");
        user.setPassword("WEWE22");

        userRepository.save(user);

        assertThat(user.getId()).isGreaterThan(0);
    }
}
