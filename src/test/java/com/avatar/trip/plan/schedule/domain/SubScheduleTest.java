package com.avatar.trip.plan.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.avatar.trip.plan.common.domain.Amount;
import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.BusinessException;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.exception.UnauthorizedException;
import com.avatar.trip.plan.exception.WrongDateException;
import com.avatar.trip.plan.theme.domain.Theme;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SubScheduleTest {

    private static final Theme THEME = Theme.of("가족과", 1L);
    private static final Schedule MAIN_SCHEDULE = Schedule.of(1L, 1L,
        List.of(ScheduleTheme.of(THEME)), Period.of(1,2));

    @Test
    void created() {
        SubSchedule schedule = SubSchedule.of(1, 1L, 1, MAIN_SCHEDULE);

        assertThat(schedule.getDay()).isEqualTo(Days.valueOf(1));
    }

    @Test
    void created_failedBecauseNullArgs() {

        assertThatThrownBy(() -> SubSchedule.of(null, 1L, 1, MAIN_SCHEDULE))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> SubSchedule.of(1, null, 1, MAIN_SCHEDULE))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> SubSchedule.of(1, 1L, null, MAIN_SCHEDULE))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> SubSchedule.of(1, 1L, 1, null))
            .isInstanceOf(RequiredArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    void created_failedBecauseDay(int input) {
        assertThatThrownBy(() -> SubSchedule.of(input, 1L, 1, MAIN_SCHEDULE))
            .isInstanceOf(WrongDateException.class);
    }

    @Test
    void created_failedBecauseDayNotBetweenMainSchedule() {
        assertThatThrownBy(() -> SubSchedule.of(10, 1L, 1, MAIN_SCHEDULE))
            .isInstanceOf(WrongDateException.class)
            .hasMessage(String.format("일차는 여행기간 내로 입력하세요. (여행기간: %s 일)", MAIN_SCHEDULE.getDays()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})
    void created_failedBecauseOrder(int input) {
        assertThatThrownBy(() -> SubSchedule.of(1, 1L, input, MAIN_SCHEDULE))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    void inputBudget() {
        SubSchedule schedule = SubSchedule.of(1, 1L, 1, MAIN_SCHEDULE);

        assertThat(schedule.getBudget()).isNull();

        schedule.inputBudget(BigDecimal.valueOf(10000));

        assertThat(schedule.getBudget()).isEqualTo(Amount.valueOf(10000));
    }

    @Test
    void canEdit_failed() {
        SubSchedule schedule = SubSchedule.of(1, 1L, 1, MAIN_SCHEDULE);

        assertThatThrownBy(() -> schedule.canEdit(2L))
            .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void canRead_failed() {
        SubSchedule schedule = SubSchedule.of(1, 1L, 1, MAIN_SCHEDULE);

        assertThatThrownBy(() -> schedule.canRead(2L))
            .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void makeANote() {
        SubSchedule schedule = SubSchedule.of(1, 1L, 1, MAIN_SCHEDULE);

        assertThat(schedule.getNote()).isNull();

        schedule.takeNote("note");

        assertThat(schedule.toStringNote()).isEqualTo("note");
    }
}