package com.avatar.trip.plan.schedule.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ScheduleThemes {

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<ScheduleTheme> themes = new ArrayList<>();


    public void addTheme(ScheduleTheme theme) {
        if (!contains(theme)){
            themes.add(theme);
        }
    }

    public boolean contains(ScheduleTheme theme) {
        return this.themes.contains(theme);
    }

    public void remove(ScheduleTheme theme) {
        if(contains(theme)){
            this.themes.remove(theme);
        }
    }
}
