package com.avatar.trip.plan.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.exception.CannotDeleteException;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeTest {

    static final Long ADMIN_ID = 1L;
    static final Long OWNER_ID = 2L;

    @ParameterizedTest
    @ValueSource(strings = {"아이와", "친구와", "가족과"})
    void createTheme(String value) {
        Theme theme = Theme.of(value, OWNER_ID);
        Theme adminTheme = Theme.ofAdmin(value, ADMIN_ID);
        assertAll(
            () -> assertFalse(theme.getIsAdmin()),
            () -> assertThat(theme.getName()).isEqualTo(ThemeName.valueOf(value)),
            () -> assertTrue(adminTheme.getIsAdmin()),
            () -> assertThat(adminTheme.getName()).isEqualTo(ThemeName.valueOf(value))
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void createEmptyTheme_failed(String value) {
        assertThatThrownBy(() -> Theme.of(value, OWNER_ID))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Theme.ofAdmin(value, ADMIN_ID))
            .isInstanceOf(RequiredArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void createEmptyOwner_failed(Long owner) {
        assertThatThrownBy(() -> Theme.of("아이와", owner))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Theme.ofAdmin("친구와", owner))
            .isInstanceOf(RequiredArgumentException.class);
    }

    @Test
    void sameOwner() {
        Theme theme = Theme.of("아이와", OWNER_ID);

        assertAll(
            () -> assertTrue(theme.isOwner(OWNER_ID)),
            () -> assertFalse(theme.isOwner(ADMIN_ID))
        );
    }

    @Test
    void validateDelete() {
        Theme theme = Theme.of("아이와", OWNER_ID);

        assertThatThrownBy(() -> theme.validateDelete(ADMIN_ID))
            .isInstanceOf(CannotDeleteException.class);
    }
}