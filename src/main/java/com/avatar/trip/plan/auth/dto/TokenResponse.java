package com.avatar.trip.plan.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String refreshToken;

    private TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenResponse of(String accessToken, String refreshToken){
        return new TokenResponse(accessToken, refreshToken);
    }
}
