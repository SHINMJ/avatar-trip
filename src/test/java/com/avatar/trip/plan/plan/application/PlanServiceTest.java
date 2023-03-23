package com.avatar.trip.plan.plan.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.avatar.trip.plan.plan.domain.Period;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanRepository;
import com.avatar.trip.plan.plan.domain.PlanTheme;
import com.avatar.trip.plan.plan.dto.PlanRequest;
import com.avatar.trip.plan.plan.dto.PlanResponse;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.theme.domain.ThemeRepository;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {
    private static final Theme THEME_WITH_CHILD = Theme.of("아이와", 1L);

    @Mock
    private PlanRepository planRepository;
    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private PlanService service;

    LoginUser loginUser = mock(LoginUser.class);

    @Test
    void createdWithPeriod() {
        PlanRequest request = new PlanRequest(1L, List.of(1L),1, 2, null, null);

        PlanResponse response = created(request);

        assertThat(response.getPeriod()).isEqualTo("1박 2일");
    }

    @Test
    void createdWithDate() {
        LocalDate startDate = LocalDate.of(2023, 01, 01);
        PlanRequest request = new PlanRequest(1L, List.of(1L),null, null, startDate, startDate.plusDays(1));
        PlanResponse response = created(request);
        assertThat(response.getPeriod()).isEqualTo("1박 2일");
    }

    private PlanResponse created(PlanRequest request){
        Plan plan = request.toPlan(1L, List.of(PlanTheme.of(THEME_WITH_CHILD)));

        when(loginUser.getId())
            .thenReturn(1L);
        when(themeRepository.findByIdIn(anyList()))
            .thenReturn(List.of(THEME_WITH_CHILD));
        when(planRepository.save(any(Plan.class)))
            .thenReturn(plan);

        PlanResponse response = service.created(loginUser, request);

        return response;
    }

    @Test
    void updatePlace() {
        Plan plan = Plan.of(1L, 1L, List.of(PlanTheme.of(THEME_WITH_CHILD)), Period.of(1,2));

        when(loginUser.getId())
            .thenReturn(1L);
        when(planRepository.findById(anyLong()))
            .thenReturn(Optional.of(plan));
//        when(scheduleRepository.save(any(Schedule.class)))
//            .thenReturn(schedule);

        PlanResponse response = service.updatePlace(loginUser, 1L, 2L);

        assertThat(response.getPlaceId()).isEqualTo(2L);
    }

    @Test
    void findResponseById() {
        Plan plan = Plan.of(1L, 1L, List.of(PlanTheme.of(THEME_WITH_CHILD)), Period.of(1,2));

        when(loginUser.getId())
            .thenReturn(1L);
        when(planRepository.findById(anyLong()))
            .thenReturn(Optional.of(plan));

        PlanResponse response = service.findResponseById(loginUser, 1L);

        assertThat(response.getThemes()).containsExactly(THEME_WITH_CHILD.getThemeName());
    }
}