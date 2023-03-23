package com.avatar.trip.plan.plan.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.exception.WrongDateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PeriodTest {

    @ParameterizedTest
    @CsvSource(value = {"1,2", "2,3", "3,4"}, delimiter = ',')
    void created(int night, int day) {
        Period period = Period.of(night, day);

        assertThat(period.toString()).isEqualTo(night+"박 "+day+"일");
    }


    @ParameterizedTest
    @CsvSource(value = {"2,1", "3,2", "4,1"}, delimiter = ',')
    void created_failed_nightGreaterThanDays(int night, int day) {
        assertThatThrownBy(() -> Period.of(night, day))
            .isInstanceOf(WrongDateException.class);
    }

    @Test
    void contain() {
        Period period = Period.of(2, 3);

        assertAll(
            () -> assertTrue(period.contains(1)),
            () -> assertTrue(period.contains(2)),
            () -> assertTrue(period.contains(3)),
            () -> assertFalse(period.contains(4)),
            () -> assertFalse(period.contains(0))
        );

    }
}