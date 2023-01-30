package com.avatar.trip.plan.user.dto;

import com.avatar.trip.plan.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public class UserResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final boolean activate;

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), user.isActivate());
    }
}
