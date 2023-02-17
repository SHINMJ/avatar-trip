package com.avatar.trip.plan.common.domain;

import com.avatar.trip.plan.exception.WrongDateException;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Days {
    private static final int MIN_VALUE = 0;

    private Integer days;

    private Days(int days){
        validate(days);
        this.days = days;
    }

    private Days(String days){
        int parse = parseInt(days);
        validate(parse);
        this.days = parse;
    }

    public static Days valueOf(int days){
        return new Days(days);
    }

    public static Days valueOf(String days){
        return new Days(days);
    }

    private void validate(int days) {
        if (days < MIN_VALUE){
            throw new WrongDateException();
        }
    }

    private int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new WrongDateException("날짜는 숫자를 입력해 주세요.");
        }
    }

    public int compareTo(Integer days){
        return this.days.compareTo(days);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Days days1 = (Days) o;
        return days.equals(days1.days);
    }

    @Override
    public int hashCode() {
        return Objects.hash(days);
    }

    @Override
    public String toString() {
        return days.toString();
    }

}
