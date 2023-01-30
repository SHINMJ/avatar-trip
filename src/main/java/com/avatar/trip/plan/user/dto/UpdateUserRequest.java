package com.avatar.trip.plan.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserRequest {
    private final String password;
    private final String nickname;

}
