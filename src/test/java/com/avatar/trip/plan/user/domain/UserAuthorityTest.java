package com.avatar.trip.plan.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class UserAuthorityTest {
    private static final User TEST_USER = User.of("testuser", "1234@");
    private static final Authority TEST_AUTHORITY = Authority.from(Role.ADMIN);

    @Test
    void createUserAuthoritySuccess() {
        UserAuthority userAuthority = UserAuthority.of(TEST_AUTHORITY);
        TEST_USER.addAuthority(userAuthority);

        assertAll(
            () -> assertTrue(userAuthority.equalsUser(TEST_USER)),
            () -> assertTrue(userAuthority.equalsAuthority(TEST_AUTHORITY)),
            () -> assertTrue(TEST_USER.containAuthority(userAuthority))
        );
    }

    @ParameterizedTest
    @NullSource
    void createUserAuthorityNullFailed(Object input){
        assertThatThrownBy(() -> UserAuthority.of((Authority) input))
            .isInstanceOf(RequiredArgumentException.class)
            .hasMessageContaining("권한");


    }
}