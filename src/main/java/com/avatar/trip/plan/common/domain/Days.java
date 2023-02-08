package com.avatar.trip.plan.common.domain;

import com.avatar.trip.plan.exception.WrongDateException;
import java.util.Objects;

public final class Days {

    private final Integer days;

    private Days(int days){
        this.days = days;
    }

    private Days(String days){
        this.days = parseInt(days);
    }

    public static Days valueOf(int days){
        return new Days(days);
    }

    public static Days valueOfString(String days){
        return new Days(days);
    }

    private int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new WrongDateException("날짜는 숫자를 입력해 주세요.");
        }
    }

    public boolean greaterThan(Days day) {
        if (this.days.compareTo(day.days) > 0){
            return true;
        }
        return false;
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
