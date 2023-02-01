package com.avatar.trip.plan.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenRequest {
    private final String accessToken;
    private final String refreshToken;
}
