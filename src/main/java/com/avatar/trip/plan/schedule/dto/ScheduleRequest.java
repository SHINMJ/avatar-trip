package com.avatar.trip.plan.schedule.dto;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.schedule.domain.Period;
import com.avatar.trip.plan.schedule.domain.PeriodDate;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleTheme;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleRequest {

    private final Long placeId;
    private final List<Long> themeIds;
    private final Integer night;
    private final Integer days;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Schedule toSchedule(Long ownerId, List<ScheduleTheme> themes){
        if (startDate != null && endDate != null){
            return Schedule.ofDate(ownerId, placeId, themes, PeriodDate.of(startDate, endDate));
        }
        return Schedule.of(ownerId, placeId, themes, Period.of(Days.valueOf(night), Days.valueOf(days)));
    }

}
