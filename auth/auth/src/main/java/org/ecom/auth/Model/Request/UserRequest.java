package org.ecom.auth.Model.Request;

import jakarta.validation.constraints.*;

public class UserRequest {

        @NotBlank(message = "Name is required")
    @NotNull(message = "Name can't be Null")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters and spaces")
    private String name;

        @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @NotNull(message = "Email can't be Null")
    private String email;

        @NotBlank(message = "Password is required")
    @Size(min = 6, max = 6, message = "Password must be exactly 6 characters long")
    @Pattern(regexp = "^[A-Za-z0-9]{6}$", message = "Password must contain only letters and Digits Password must be exactly 6 characters long")
    private String password;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
