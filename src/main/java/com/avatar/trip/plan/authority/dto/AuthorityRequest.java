package com.avatar.trip.plan.authority.dto;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.common.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@RequiredArgsConstructor
public class AuthorityRequest {
    private final String roleId;
    private final String name;

    public Authority toAuthority(){
        if (StringUtils.hasText(this.name)){
            return Authority.of(Role.findByKey(roleId), name);
        }
        return Authority.from(Role.findByKey(roleId));
    }
}
