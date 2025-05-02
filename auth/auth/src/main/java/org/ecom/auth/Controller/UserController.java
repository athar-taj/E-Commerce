package org.ecom.auth.Controller;

import org.ecom.auth.Config.JWTConfig;
import org.ecom.auth.Model.Request.UserRequest;
import org.ecom.auth.Model.Response.CommonResponse;
import org.ecom.auth.Model.User;
import org.ecom.auth.Repository.UserRepository;
import org.ecom.auth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTConfig jwtConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> register(@RequestBody UserRequest user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody UserRequest request) {
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken
                            (request.getEmail(), request.getPassword()));
            Optional<User> user = userRepository.findByEmail(request.getEmail());
            if (user.isPresent()) {
                String token = jwtConfig.generateToken(user.get().getEmail());
                return ResponseEntity.ok(new CommonResponse(200, "Login successful!", token));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse(404, "Login failed! User not found !",null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponse(500, "Login Failed!!",e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<CommonResponse> forgotPassword(@RequestBody UserRequest user) {
        return userService.forgotPassword(user);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<CommonResponse> verifyToken(@RequestHeader("Authorization") String authHeader) {
        return userService.validateToken(authHeader);
    }

    @GetMapping("/available/{userId}")
    public Boolean isAvailableUser(@PathVariable Long userId ){
        return userService.isUserAvailable(userId);
    }
}
