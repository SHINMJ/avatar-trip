package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.theme.domain.Theme;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ScheduleTheme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne()
    @JoinColumn(name = "theme_id")
    private Theme theme;

    private ScheduleTheme(Theme theme){
        validate(theme);
        this.theme = theme;
    }

    private void validate(Theme theme) {
        if (theme == null){
            throw new RequiredArgumentException("테마");
        }
    }

    public static ScheduleTheme of(Theme theme){
        return new ScheduleTheme(theme);
    }

    public boolean equalSchedule(Schedule schedule) {
        if(this.schedule == null){
            return false;
        }
        return this.schedule.equals(schedule);
    }


    public void setSchedule(Schedule schedule) {
        if(this.schedule != null){
            this.schedule.removeTheme(this);
        }

        this.schedule = schedule;

        if(!this.schedule.containTheme(this)){
            this.schedule.addTheme(this);
        }
    }

    public void removeSchedule() {
        if(this.schedule == null){
            return;
        }
        this.schedule.removeTheme(this);
        this.schedule = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleTheme that = (ScheduleTheme) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
