package com.avatar.trip.plan.party.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.schedule.domain.Period;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleTheme;
import com.avatar.trip.plan.theme.domain.Theme;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class PartyTest {
    private static final List<ScheduleTheme> THEMES = List.of(ScheduleTheme.of(Theme.of("테마", 1L)));
    private static final Schedule SCHEDULE = Schedule.of(1L, 1L, THEMES, Period.of(Days.valueOf(1),Days.valueOf(2)));


    @Test
    void created() {
        Party party = Party.of(PhoneNumber.valueOf("010-1111-1111"), Permission.EDIT, SCHEDULE);

        assertThat(party.getSend()).isFalse();
    }

    @DisplayName("일행 생성 실패 - null")
    @ParameterizedTest
    @NullSource
    void created_failed(Object input) {
        assertThatThrownBy(() -> Party.of((PhoneNumber) input, Permission.EDIT, SCHEDULE))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Party.of(PhoneNumber.valueOf("01011111111"), (Permission) input, SCHEDULE))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT,
            (Schedule) input))
            .isInstanceOf(RequiredArgumentException.class);
    }

    @Test
    void setUserId() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, SCHEDULE);

        assertThat(party.getUserId()).isNull();

        party.setUserId(1L);

        assertThat(party.getUserId()).isNotNull();
    }

    @Test
    void sendSMS() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, SCHEDULE);

        assertThat(party.getSend()).isFalse();

        party.sendSMS();

        assertThat(party.getSend()).isTrue();

    }

    @Test
    void updatePermission() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, SCHEDULE);

        assertThat(party.getPermission()).isEqualTo(Permission.EDIT);

        party.updatePermission(Permission.READ);

        assertThat(party.getPermission()).isEqualTo(Permission.READ);
    }
}