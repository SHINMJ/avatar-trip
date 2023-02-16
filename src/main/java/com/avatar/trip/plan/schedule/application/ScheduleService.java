package com.avatar.trip.plan.schedule.application;

import com.avatar.trip.plan.exception.NotFoundException;
import com.avatar.trip.plan.exception.UnauthorizedException;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleRepository;
import com.avatar.trip.plan.schedule.domain.ScheduleTheme;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.theme.domain.ThemeRepository;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository repository;
    private final ThemeRepository themeRepository;


    public ScheduleResponse created(LoginUser loginUser, ScheduleRequest request) {
        List<Theme> themes = themeRepository.findByIdIn(request.getThemeIds());
        List<ScheduleTheme> scheduleThemes = themes.stream().map(ScheduleTheme::of)
            .collect(Collectors.toList());

        Schedule saved = repository.save(request.toSchedule(loginUser.getId(), scheduleThemes));

        return ScheduleResponse.of(saved);
    }

    public ScheduleResponse updatePlace(LoginUser loginUser, Long id, Long placeId) {
        Schedule schedule = findById(id);

        schedule.canEdit(loginUser.getId());

        schedule.updatePlace(placeId);

        return ScheduleResponse.of(schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findResponseById(LoginUser loginUser, Long id) {
        Schedule schedule = findById(id);

        schedule.canRead(loginUser.getId());

        return ScheduleResponse.of(schedule);
    }

    private Schedule findById(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 일정을 찾을 수 없습니다."));
    }
}
