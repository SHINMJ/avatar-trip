package com.avatar.trip.plan.schedule.dto;

import com.avatar.trip.plan.schedule.domain.PeriodDate;
import com.avatar.trip.plan.schedule.domain.Schedule;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public class ScheduleResponse {

    private final Long id;
    private final Long placeId;
    private final List<String> themes;
    private final String period;
    private final PeriodDate periodDate;

    public static ScheduleResponse of(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), schedule.getPlaceId(),
            schedule.getThemeNames(), schedule.getPeriodString(), schedule.getPeriodDate());
    }
}
