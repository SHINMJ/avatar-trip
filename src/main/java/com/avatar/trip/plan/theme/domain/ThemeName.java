package com.avatar.trip.plan.theme.domain;

import com.avatar.trip.plan.exception.RequiredArgumentException;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ThemeName {
    private String name;

    private ThemeName(String name){
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if(!StringUtils.hasText(name)){
            throw new RequiredArgumentException("테마를 입력해 주세요.");
        }
    }

    static ThemeName valueOf(String name){
        return new ThemeName(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ThemeName themeName = (ThemeName) o;
        return name.equals(themeName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
