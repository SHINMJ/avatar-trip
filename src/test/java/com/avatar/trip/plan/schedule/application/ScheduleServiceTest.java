package com.avatar.trip.plan.schedule.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.schedule.domain.Period;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleRepository;
import com.avatar.trip.plan.schedule.domain.ScheduleTheme;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
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
class ScheduleServiceTest {
    private static final Theme THEME_WITH_CHILD = Theme.of("아이와", 1L);

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ScheduleService service;

    LoginUser loginUser = mock(LoginUser.class);

    @Test
    void createdWithPeriod() {
        ScheduleRequest request = new ScheduleRequest(1L, List.of(1L),1, 2, null, null);

        ScheduleResponse response = created(request);

        assertThat(response.getPeriod()).isEqualTo("1박 2일");
    }

    @Test
    void createdWithDate() {
        LocalDate startDate = LocalDate.of(2023, 01, 01);
        ScheduleRequest request = new ScheduleRequest(1L, List.of(1L),null, null, startDate, startDate.plusDays(1));
        ScheduleResponse response = created(request);
        assertThat(response.getPeriod()).isEqualTo("1박 2일");
    }

    private ScheduleResponse created(ScheduleRequest request){
        Schedule schedule = request.toSchedule(1L, List.of(ScheduleTheme.of(THEME_WITH_CHILD)));

        when(loginUser.getId())
            .thenReturn(1L);
        when(themeRepository.findByIdIn(anyList()))
            .thenReturn(List.of(THEME_WITH_CHILD));
        when(scheduleRepository.save(any(Schedule.class)))
            .thenReturn(schedule);

        ScheduleResponse response = service.created(loginUser, request);

        return response;
    }

    @Test
    void updatePlace() {
        Schedule schedule = Schedule.of(1L, 1L, List.of(ScheduleTheme.of(THEME_WITH_CHILD)), Period.of(
            Days.valueOf(1), Days.valueOf(2)));

        when(loginUser.getId())
            .thenReturn(1L);
        when(scheduleRepository.findById(anyLong()))
            .thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class)))
            .thenReturn(schedule);

        ScheduleResponse response = service.updatePlace(loginUser, 1L, 2L);

        assertThat(response.getPlaceId()).isEqualTo(2L);
    }

    @Test
    void findResponseById() {
        Schedule schedule = Schedule.of(1L, 1L, List.of(ScheduleTheme.of(THEME_WITH_CHILD)), Period.of(
            Days.valueOf(1), Days.valueOf(2)));

        when(loginUser.getId())
            .thenReturn(1L);
        when(scheduleRepository.findById(anyLong()))
            .thenReturn(Optional.of(schedule));

        ScheduleResponse response = service.findResponseById(loginUser, 1L);

        assertThat(response.getThemes()).containsExactly(THEME_WITH_CHILD.getThemeName());
    }
}