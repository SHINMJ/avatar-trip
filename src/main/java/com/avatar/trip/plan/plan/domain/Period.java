package com.avatar.trip.plan.plan.domain;

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
    private static final int MIN_VALUE = 1;
    private static final String NIGHT = "박";
    private static final String DAYS = "일";

    @Embedded
    @AttributeOverride(name = "days", column = @Column(name = "night"))
    private Days night;

    @Embedded
    @AttributeOverride(name = "days", column = @Column(name = "days"))
    private Days day;

    private Period(Integer night, Integer day) {
        validate(night, day);
        this.night = Days.valueOf(night);
        this.day = Days.valueOf(day);
    }

    private void validate(Integer night, Integer day) {
        if (night.compareTo(day) > -1){
            throw new WrongDateException("기간을 잘못 입력하셨습니다.");
        }
    }

    public static Period of(Integer night, Integer day){
        return new Period(night, day);
    }

    public boolean contains(Integer days) {
        return days >= MIN_VALUE && this.day.compareTo(days) >= 0;
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
