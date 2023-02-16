package com.avatar.trip.plan.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.WrongDateException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PeriodDateTest {

    @ParameterizedTest
    @CsvSource(value = {"2022;11;11;2","2022;12;22;5","2023;1;11;20"}, delimiter = ';')
    void created(int year, int month, int day, int plusDay) {
        LocalDate startDate = LocalDate.of(year, month, day);
        LocalDate endDate = startDate.plusDays(plusDay);

        PeriodDate periodDate = PeriodDate.of(startDate, endDate);
    }

    @ParameterizedTest
    @CsvSource(value = {"2022;11;11;2","2022;12;22;5","2023;1;11;20"}, delimiter = ';')
    void created_failed(int year, int month, int day, int plusDay) {
        LocalDate startDate = LocalDate.of(year, month, day);
        LocalDate endDate = startDate.plusDays(plusDay);
        assertThatThrownBy(() -> PeriodDate.of(endDate, startDate))
            .isInstanceOf(WrongDateException.class);
    }

    @Test
    void toPeriod() {
        LocalDate startDate = LocalDate.of(2023, 1, 2);
        LocalDate endDate = startDate.plusDays(10);

        PeriodDate periodDate = PeriodDate.of(startDate, endDate);
        Period toPeriod = periodDate.toPeriod();

        assertThat(toPeriod).isEqualTo(Period.of(10,11));
    }
}