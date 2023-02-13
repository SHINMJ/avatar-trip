package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.WrongDateException;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public final class Period {
    private static final String NIGHT = "박";
    private static final String DAYS = "일";

    @Embedded
    @AttributeOverride(name = "days", column = @Column(name = "night"))
    private Days night;

    @Embedded
    @AttributeOverride(name = "days", column = @Column(name = "days"))
    private Days day;

    private Period(Days night, Days day) {
        validate(night, day);
        this.night = night;
        this.day = day;
    }

    private void validate(Days night, Days day) {
        if (night.greaterThan(day)){
            throw new WrongDateException("기간을 잘못 입력하셨습니다.");
        }
    }

    public static Period of(Days night, Days day){
        return new Period(night, day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Period period = (Period) o;
        return night.equals(period.night) && day.equals(period.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(night, day);
    }

    @Override
    public String toString() {
        return night+NIGHT+" "+day+DAYS;
    }
}
