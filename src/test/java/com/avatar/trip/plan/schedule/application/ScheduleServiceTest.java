package com.avatar.trip.plan.schedule.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.CannotDeleteException;
import com.avatar.trip.plan.plan.application.PlanService;
import com.avatar.trip.plan.plan.domain.Period;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanTheme;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleRepository;
import com.avatar.trip.plan.schedule.dto.ScheduleBudgetRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleNoteRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

        when(planService.findCanReadPlanById(loginUser, 1L))
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

    @Test
    void findAllByPlan() {
        when(planService.findCanReadPlanById(loginUser, 1L))
            .thenReturn(plan);
        when(loginUser.getId())
            .thenReturn(1L);
        when(scheduleRepository.findAllByPlanOrderByDayAscOrderAsc(any(Plan.class)))
            .thenReturn(List.of(Schedule.of(1, 1L, 1, plan), Schedule.of(1, 2L, 2, plan), Schedule.of(2, 3L, 1, plan)));

        List<ScheduleResponse> responses = service.findAllByPlan(loginUser, 1L);

        assertThat(responses.size()).isEqualTo(3);
    }

    @Test
    void inputBudget() {
        BigDecimal budget = BigDecimal.valueOf(100000L);
        ScheduleBudgetRequest request = new ScheduleBudgetRequest(budget);
        Schedule schedule = Schedule.of(1, 1L, 1, plan);

        when(loginUser.getId())
            .thenReturn(1L);
        when(scheduleRepository.findById(anyLong()))
            .thenReturn(Optional.of(schedule));

        ScheduleResponse response = service.inputBudget(loginUser, 1L, request);

        assertThat(response.getBudget()).isEqualTo(budget);
    }

    @Test
    void takeNotes() {
        String note = "note";
        ScheduleNoteRequest request = new ScheduleNoteRequest(note);
        Schedule schedule = Schedule.of(1, 1L, 1, plan);

        when(loginUser.getId())
            .thenReturn(1L);
        when(scheduleRepository.findById(anyLong()))
            .thenReturn(Optional.of(schedule));

        ScheduleResponse response = service.takeNotes(loginUser, 1L, request);

        assertThat(response.getNote()).isEqualTo(note);
    }

    @Test
    void deleteFailed_cannotEdit() {
        Schedule schedule = Schedule.of(1, 1L, 1, plan);
        when(loginUser.getId())
            .thenReturn(2L);
        when(scheduleRepository.findById(anyLong()))
            .thenReturn(Optional.of(schedule));

        assertThatThrownBy(() -> service.delete(loginUser, 1L))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void findTotalBudgetByDay() {
        Schedule schedule1 = Schedule.of(1, 1L, 1, plan);
        schedule1.inputBudget(BigDecimal.valueOf(14000));
        Schedule schedule2 = Schedule.of(1, 2L, 2, plan);
        schedule2.inputBudget(BigDecimal.valueOf(20000));

        when(loginUser.getId())
            .thenReturn(1L);
        when(planService.findCanReadPlanById(loginUser, 1L))
            .thenReturn(plan);
        when(scheduleRepository.findByPlanAndDayOrderByOrder(any(Plan.class), any(Days.class)))
            .thenReturn(List.of(schedule1, schedule2));


        BigDecimal totalBudgetByDay = service.findTotalBudgetByDay(loginUser, 1L, 1);

        assertThat(totalBudgetByDay).isEqualTo(BigDecimal.valueOf(34000));
    }
}