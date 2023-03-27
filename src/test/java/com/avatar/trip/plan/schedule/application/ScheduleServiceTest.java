package com.avatar.trip.plan.schedule.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.avatar.trip.plan.plan.application.PlanService;
import com.avatar.trip.plan.plan.domain.Period;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanTheme;
import com.avatar.trip.plan.plan.dto.PlanResponse;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleRepository;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    private static final Theme THEME_WITH_CHILD = Theme.of("아이와", 1L);
    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    PlanService planService;

    @InjectMocks
    ScheduleService service;

    LoginUser loginUser = mock(LoginUser.class);
    Plan plan = Plan.of(1L, 1L, List.of(PlanTheme.of(THEME_WITH_CHILD)), Period.of(1,2));

    @Test
    void createSchedule() {
        ScheduleRequest request = new ScheduleRequest(1, 1L, 1,1L);

        when(planService.findPlanById(loginUser, 1L))
            .thenReturn(plan);
        when(loginUser.getId())
            .thenReturn(1L);
        when(scheduleRepository.save(any(Schedule.class)))
            .thenReturn(Schedule.of(1, 1L, 1, plan));

        ScheduleResponse response = service.create(loginUser, request);

        assertAll(
            () -> assertThat(response.getDay()).isEqualTo(request.getDay()),
            () -> assertThat(response.getOrder()).isEqualTo(request.getOrder())
        );
    }
}