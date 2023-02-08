package com.avatar.trip.plan.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.theme.domain.Theme;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ScheduleTest {

    static final Theme USER_THEME = Theme.of("아이와", 1L);
    static final int DATE_BETWEEN = 10;
    static final LocalDate START_DATE = LocalDate.of(2023, 1, 2);
    static final LocalDate END_DATE = START_DATE.plusDays(DATE_BETWEEN);

    @Test
    void createdWithPeriod() {
        Period period = Period.of(Days.valueOf(2), Days.valueOf(3));

        Schedule schedule = Schedule.of(1L, 1L, List.of(ScheduleTheme.of(USER_THEME)), period);
    }

    @Test
    void createdWithPeriodDate() {
        PeriodDate periodDate = PeriodDate.of(START_DATE, END_DATE);

        Schedule schedule = Schedule.ofDate(1L, 1L, List.of(ScheduleTheme.of(USER_THEME)), periodDate);

        assertThat(schedule.getPeriod()).isEqualTo(Period.of(Days.valueOf(DATE_BETWEEN-1), Days.valueOf(DATE_BETWEEN)));
    }

    @Test
    void created_failed() {
        Period period = Period.of(Days.valueOf(DATE_BETWEEN-1), Days.valueOf(DATE_BETWEEN));

        assertThatThrownBy(() -> Schedule.of(1L, 1L, List.of(ScheduleTheme.of(USER_THEME)), null))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Schedule.of(1L, null, List.of(ScheduleTheme.of(USER_THEME)), period))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Schedule.of(1L, 1L, null, period))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Schedule.ofDate(1L, 1L, List.of(ScheduleTheme.of(USER_THEME)), null))
            .isInstanceOf(RequiredArgumentException.class);
    }
}