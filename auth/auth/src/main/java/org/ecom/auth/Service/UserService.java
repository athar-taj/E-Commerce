package org.ecom.auth.Service;

import org.ecom.auth.Model.Request.UserRequest;
import org.ecom.auth.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<CommonResponse> register(UserRequest user);
    ResponseEntity<CommonResponse> login(UserRequest user);
    ResponseEntity<CommonResponse> forgotPassword(UserRequest user);
    ResponseEntity<CommonResponse> validateToken(String authHeader);
    boolean isUserAvailable(Long userId);

}
