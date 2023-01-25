package com.avatar.trip.plan.common.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ANONYMOUS("ROLE_ANONYMOUS"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String key;

    public static Role findByKey(String key){
        return Arrays.stream(Role.values())
            .filter(role -> role.equalKey(key))
            .findAny().orElse(null);
    }

    private boolean equalKey(String key) {
        return this.key.equals(key);
    }
}
