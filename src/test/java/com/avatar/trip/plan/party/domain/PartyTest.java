package com.avatar.trip.plan.party.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.exception.PhoneNumberException;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

class PartyTest {

    @Test
    void created() {
        Party party = Party.of(PhoneNumber.valueOf("010-1111-1111"), Permission.EDIT, 1L);

        assertThat(party.getSend()).isFalse();
    }

    @DisplayName("일행 생성 실패 - null")
    @ParameterizedTest
    @NullSource
    void created_failed(Object input) {
        assertThatThrownBy(() -> Party.of((PhoneNumber) input, Permission.EDIT, 1L))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Party.of(PhoneNumber.valueOf("01011111111"), (Permission) input, 1L))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT,
            (Long) input))
            .isInstanceOf(RequiredArgumentException.class);
    }

    @Test
    void setUserId() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, 1L);

        assertThat(party.getUserId()).isNull();

        party.setUserId(1L);

        assertThat(party.getUserId()).isNotNull();
    }

    @Test
    void sendSMS() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, 1L);

        assertThat(party.getSend()).isFalse();

        party.sendSMS();

        assertThat(party.getSend()).isTrue();

    }
}