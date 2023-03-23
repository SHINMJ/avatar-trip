package com.avatar.trip.plan.plan.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PlanThemes {

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<PlanTheme> themes = new ArrayList<>();


    public void addTheme(PlanTheme theme) {
        if (!contains(theme)){
            themes.add(theme);
        }
    }

    public boolean contains(PlanTheme theme) {
        return this.themes.contains(theme);
    }

    public void remove(PlanTheme theme) {
        this.themes.remove(theme);
    }

    public List<String> getThemeNames() {
        return this.themes.stream()
            .map(PlanTheme::getThemeName)
            .collect(Collectors.toList());
    }
}
