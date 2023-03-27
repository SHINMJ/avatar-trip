package com.avatar.trip.plan.schedule.dto;

import com.avatar.trip.plan.schedule.domain.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleResponse {
    private final Long id;
    private final int day;
    private final Long placeId;
    private final int order;

    public static ScheduleResponse of(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), schedule.toIntDay(), schedule.getPlaceId(), schedule.toIntOrder());
    }
}
