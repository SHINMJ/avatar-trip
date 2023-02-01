package com.avatar.trip.plan.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UserTest {

    @ParameterizedTest
    @CsvSource(value = {"신명진;1234", "32123;11122", "shinmj;sdkfi2@s"}, delimiter = ';')
    void createUserSuccess(String username, String password) {
        User user = User.of(username, password);

        assertAll(
            () -> assertThat(user.getEmail()).isEqualTo(username),
            () -> assertThat(user.getPassword()).isEqualTo(password)
        );
    }

    @ParameterizedTest
    @CsvSource(value = { ";11122", "shinmj;"}, delimiter = ';')
    void createUserFailed(String username, String password) {
        assertThatThrownBy(() -> User.of(username, password))
            .isInstanceOf(RequiredArgumentException.class);
    }

    @Test
    void createUserWithAuthorities() {
        UserAuthority authAdmin = UserAuthority.of(Authority.from(Role.ADMIN));
        UserAuthority authUser = UserAuthority.of(Authority.from(Role.USER));

        User user = User.of("email", "11111", "test", List.of(authAdmin, authUser));

        assertAll(
            () -> assertThat(user.getUserAuthorities().size()).isEqualTo(2),
            () -> assertThat(user.getUserAuthorities()).containsExactly(authAdmin, authUser),
            () -> assertTrue(authAdmin.equalsUser(user)),
            () -> assertTrue(authUser.equalsUser(user))
        );
    }

    @Test
    void updateActive() {
        User user = User.of("email", "11111");
        assertTrue(user.isActivate());

        user.updateActivate(false);

        assertFalse(user.isActivate());
    }
}