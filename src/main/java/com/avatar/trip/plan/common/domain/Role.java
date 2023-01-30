package com.avatar.trip.plan.common.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ANONYMOUS("ROLE_ANONYMOUS", "손님"),
    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;

    public static Role findByKey(String key){
        return Arrays.stream(Role.values())
            .filter(role -> role.equalKey(key))
            .findAny().orElse(null);
    }

    private boolean equalKey(String key) {
        return this.key.equals(key);
    }
}
