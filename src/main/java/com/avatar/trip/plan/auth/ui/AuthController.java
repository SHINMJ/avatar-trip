package com.avatar.trip.plan.auth.ui;

import com.avatar.trip.plan.auth.application.AuthService;
import com.avatar.trip.plan.auth.dto.LoginRequest;
import com.avatar.trip.plan.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest){
        TokenResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
