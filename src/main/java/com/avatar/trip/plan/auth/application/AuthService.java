package com.avatar.trip.plan.auth.application;

import com.avatar.trip.plan.auth.dto.LoginRequest;
import com.avatar.trip.plan.auth.dto.TokenResponse;
import com.avatar.trip.plan.auth.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManager
            .authenticate(authenticationToken);

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return TokenResponse.of(accessToken, refreshToken);
    }
}
