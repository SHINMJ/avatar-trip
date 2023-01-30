package com.avatar.trip.plan.auth.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
