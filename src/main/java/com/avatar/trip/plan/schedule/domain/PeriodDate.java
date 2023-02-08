package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.exception.WrongDateException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class PeriodDate {

    private LocalDate startDate;
    private LocalDate endDate;

    private PeriodDate(LocalDate startDate, LocalDate endDate) {
        validate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static PeriodDate of(LocalDate startDate, LocalDate endDate) {
        return new PeriodDate(startDate, endDate);
    }

    public Period toPeriod(){
        int between = (int) ChronoUnit.DAYS.between(startDate, endDate);
        return Period.of(Days.valueOf(between-1), Days.valueOf(between));
    }

    private void validate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null){
            throw new RequiredArgumentException("시작일");
        }

        if(endDate == null){
            throw new RequiredArgumentException("종료일");
        }

        if(startDate.isAfter(endDate)){
            throw new WrongDateException("시작일이 종료일보다 늦습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeriodDate that = (PeriodDate) o;
        return startDate.equals(that.startDate) && endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
