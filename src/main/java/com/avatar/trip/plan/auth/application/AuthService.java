package com.avatar.trip.plan.auth.application;

import com.avatar.trip.plan.auth.dto.LoginRequest;
import com.avatar.trip.plan.auth.dto.TokenRequest;
import com.avatar.trip.plan.auth.dto.TokenResponse;
import com.avatar.trip.plan.auth.config.TokenProvider;
import com.avatar.trip.plan.exception.BusinessException;
import com.avatar.trip.plan.exception.TokenValidationException;
import com.avatar.trip.plan.user.application.UserService;
import com.avatar.trip.plan.user.dto.UserRequest;
import com.avatar.trip.plan.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Transactional
    public TokenResponse login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManager
            .authenticate(authenticationToken);

        return createToken(authentication);
    }

    public Long join(UserRequest userRequest) {
        UserResponse user = userService.create(userRequest);
        return user.getId();
    }

    public TokenResponse refresh(TokenRequest request){

        try {
            tokenProvider.validateToken(request.getRefreshToken());
        }catch (Exception e) {
            throw new TokenValidationException(e.getMessage());
        }

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        userService.checkRefreshTokenByEmail(authentication.getName(), request.getRefreshToken());

        return createToken(authentication);
    }

    private TokenResponse createToken(Authentication authentication){
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        userService.updateRefreshToken(String.valueOf(authentication.getName()),  refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }
}
