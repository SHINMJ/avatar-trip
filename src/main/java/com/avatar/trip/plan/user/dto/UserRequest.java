package com.avatar.trip.plan.user.dto;

import com.avatar.trip.plan.user.domain.User;
import com.avatar.trip.plan.user.domain.UserAuthority;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRequest {
    private final String email;
    private final String password;
    private final String nickname;

    public User toUser(String encodePassword, List<UserAuthority> authorities){
        return User.of(this.email, encodePassword, this.nickname, authorities);
    }
}
