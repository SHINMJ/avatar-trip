package com.avatar.trip.plan.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.WrongDateException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PeriodTest {

    @ParameterizedTest
    @CsvSource(value = {"1,2", "2,3", "3,4"}, delimiter = ',')
    void created(int night, int day) {
        Period period = Period.of(Days.valueOf(night), Days.valueOf(day));

        assertThat(period.toString()).isEqualTo(night+"박 "+day+"일");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void created_failed_nullOrEmpty(String value) {
        assertThatThrownBy(() -> Period.of(Days.valueOfString(value), Days.valueOfString(value)))
            .isInstanceOf(WrongDateException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"2,1", "3,2", "4,1"}, delimiter = ',')
    void created_failed_nightGreaterThanDays(int night, int day) {
        assertThatThrownBy(() -> Period.of(Days.valueOf(night), Days.valueOf(day)))
            .isInstanceOf(WrongDateException.class);
    }
}