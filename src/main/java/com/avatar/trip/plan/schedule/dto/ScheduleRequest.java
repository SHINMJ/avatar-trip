package com.avatar.trip.plan.schedule.dto;

import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.schedule.domain.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleRequest {
    private final int day;
    private final Long placeId;
    private final int order;
    private final Long planId;


    public Schedule toSchedule(Plan plan){
        return Schedule.of(day, placeId, order, plan);
    }
}
