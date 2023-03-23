package com.avatar.trip.plan.plan.dto;

import com.avatar.trip.plan.plan.domain.PeriodDate;
import com.avatar.trip.plan.plan.domain.Plan;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlanResponse {

    private final Long id;
    private final Long placeId;
    private final List<String> themes;
    private final String period;
    private final PeriodDate periodDate;

    public static PlanResponse of(Plan plan) {
        return new PlanResponse(plan.getId(), plan.getPlaceId(),
            plan.getThemeNames(), plan.getPeriodString(), plan.getPeriodDate());
    }
}
