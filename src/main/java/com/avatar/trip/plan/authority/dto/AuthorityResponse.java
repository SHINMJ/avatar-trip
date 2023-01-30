package com.avatar.trip.plan.authority.dto;

import com.avatar.trip.plan.authority.domain.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthorityResponse {
    private final Long id;
    private final String roleId;
    private final String name;

    public static AuthorityResponse of(Authority saved) {
        return new AuthorityResponse(saved.getId(), saved.getRoleId(), saved.getName());
    }
}
