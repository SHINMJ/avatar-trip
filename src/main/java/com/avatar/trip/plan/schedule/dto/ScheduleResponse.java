package com.avatar.trip.plan.schedule.dto;

import com.avatar.trip.plan.schedule.domain.Schedule;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleResponse {
    private final Long id;
    private final int day;
    private final Long placeId;
    private final int order;
    private final BigDecimal budget;
    private final String note;

    public static ScheduleResponse of(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), schedule.toIntDay(), schedule.getPlaceId(),
            schedule.toIntOrder(), schedule.toDecimalBudget(), schedule.toStringNote());
    }

    public static List<ScheduleResponse> ofList(List<Schedule> schedules) {
        return schedules.stream()
            .map(ScheduleResponse::of)
            .collect(Collectors.toList());
    }
}
