package com.avatar.trip.plan.theme.dto;

import com.avatar.trip.plan.theme.domain.Theme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class ThemeRequest {
    private final String theme;
    private final boolean admin;

    public Theme toTheme(Long userId){
        if(admin){
            return Theme.ofAdmin(theme, userId);
        }
        return Theme.of(theme, userId);
    }
}
