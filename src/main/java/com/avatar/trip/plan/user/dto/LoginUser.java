package com.avatar.trip.plan.user.dto;

import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.user.domain.User;
import com.avatar.trip.plan.user.infra.CustomUserDetails;

public class LoginUser extends CustomUserDetails {
    private User user;

    public LoginUser(User user) {
        super(user);
        this.user = user;
    }

    public Long getId(){
        return this.user.getId();
    }

    public String getNickname(){
        return user.getNickname();
    }

    public boolean isAdmin(){
        return user.getUserAuthorities().stream()
            .anyMatch(userAuthority -> userAuthority.equalRole(Role.ADMIN));
    }

}
