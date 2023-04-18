package com.avatar.trip.plan.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avatar.trip.plan.common.domain.SortSeq;
import com.avatar.trip.plan.plan.domain.Period;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanTheme;
import com.avatar.trip.plan.theme.domain.Theme;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SchedulesTest {
    private static final Theme THEME = Theme.of("가족과", 1L);
    private static final Plan MAIN_PLAN = Plan.of(1L, 1L,
        List.of(PlanTheme.of(THEME)), Period.of(1,2));

    @Test
    void rearrange() {
        Schedule schedule1 = Schedule.of(1, 1L, 1, MAIN_PLAN);
        Schedule schedule2 = Schedule.of(1, 1L, 2, MAIN_PLAN);
        Schedule schedule3 = Schedule.of(1, 1L, 3, MAIN_PLAN);
        Schedule schedule5 = Schedule.of(1, 1L, 5, MAIN_PLAN);
        Schedules schedules = Schedules.of(List.of(schedule1, schedule2, schedule5, schedule3));

        assertThat(schedules.getScheduleList()).extracting(Schedule::toIntOrder)
            .containsExactly(1,2,5,3);

        Schedules rearrange = schedules.rearrange();

        assertThat(rearrange.getScheduleList()).extracting(Schedule::toIntOrder)
            .containsExactly(1,2,3,4);

    }
}
