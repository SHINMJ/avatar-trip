package com.avatar.trip.plan.theme.dto;

import com.avatar.trip.plan.theme.domain.Theme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ThemeResponse {
    private final Long id;
    private final String themeName;
    private final Long ownerId;
    private final boolean admin;

    public static ThemeResponse of(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getThemeName(), theme.getOwnerId(),
            theme.getIsAdmin());
    }
}
