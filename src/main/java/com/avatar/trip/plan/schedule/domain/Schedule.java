package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Period period;

    @Embedded
    private PeriodDate periodDate;

    private Long placeId;

    @Embedded
    private ScheduleThemes themes = new ScheduleThemes();

    private Long ownerId;

//    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<Party> parties;

    private Schedule(Long ownerId, Long placeId, List<ScheduleTheme> themes, Period period) {
        validate(ownerId, period, placeId, themes);
        this.ownerId = ownerId;
        this.placeId = placeId;
        setThemes(themes);
        this.period = period;
    }

    private Schedule(Long ownerId, Long placeId, List<ScheduleTheme> themes, Period period, PeriodDate periodDate) {
        validate(ownerId, period, placeId, themes);
        this.ownerId = ownerId;
        this.placeId = placeId;
        setThemes(themes);
        this.period = period;
        this.periodDate = periodDate;
    }


    public static Schedule of(Long ownerId, Long placeId, List<ScheduleTheme> themes, Period period) {
        return new Schedule(ownerId, placeId, themes, period);
    }

    public static Schedule ofDate(Long ownerId, Long placeId, List<ScheduleTheme> themes, PeriodDate periodDate) {
        validatePeriodDate(periodDate);
        return new Schedule(ownerId, placeId, themes, periodDate.toPeriod(), periodDate);
    }

    public void addTheme(ScheduleTheme theme) {
        themes.addTheme(theme);
        if(!theme.equalSchedule(this)){
            theme.setSchedule(this);
        }
    }

    public boolean containTheme(ScheduleTheme theme) {
        return this.themes.contains(theme);
    }

    public void removeTheme(ScheduleTheme theme) {
        this.themes.remove(theme);
        if(theme.equalSchedule(this)){
            theme.removeSchedule();
        }
    }

    private static void validatePeriodDate(PeriodDate periodDate) {
        if(periodDate == null){
            throw new RequiredArgumentException("날짜");
        }
    }

    private void validate(Long ownerId, Period period, Long placeId, List<ScheduleTheme> themes) {
        if (ownerId == null){
            throw new RequiredArgumentException("사용자");
        }

        if (period == null){
            throw new RequiredArgumentException("기간");
        }

        if(placeId == null){
            throw new RequiredArgumentException("지역");
        }

        if(themes == null || themes.size() <= 0){
            throw new RequiredArgumentException("테마");
        }
    }


    private void setThemes(List<ScheduleTheme> themes) {
        for (ScheduleTheme theme: themes) {
            addTheme(theme);
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
        Schedule schedule = (Schedule) o;
        return id.equals(schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
