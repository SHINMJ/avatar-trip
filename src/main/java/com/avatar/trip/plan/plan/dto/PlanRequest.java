package com.avatar.trip.plan.plan.dto;

import com.avatar.trip.plan.plan.domain.Period;
import com.avatar.trip.plan.plan.domain.PeriodDate;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanTheme;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlanRequest {

    private final Long placeId;
    private final List<Long> themeIds;
    private final Integer night;
    private final Integer days;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Plan toPlan(Long ownerId, List<PlanTheme> themes){
        if (startDate != null && endDate != null){
            return Plan.ofDate(ownerId, placeId, themes, PeriodDate.of(startDate, endDate));
        }
        return Plan.of(ownerId, placeId, themes, Period.of(night, days));
    }

}
