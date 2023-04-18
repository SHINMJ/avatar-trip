package com.avatar.trip.plan.plan.application;

import com.avatar.trip.plan.exception.NotFoundException;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanRepository;
import com.avatar.trip.plan.plan.domain.PlanTheme;
import com.avatar.trip.plan.plan.dto.PlanRequest;
import com.avatar.trip.plan.plan.dto.PlanResponse;
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
public class PlanService {

    private final PlanRepository repository;
    private final ThemeRepository themeRepository;

    public PlanResponse create(LoginUser loginUser, PlanRequest request) {
        List<Theme> themes = themeRepository.findByIdIn(request.getThemeIds());
        List<PlanTheme> planThemes = themes.stream().map(PlanTheme::of)
            .collect(Collectors.toList());

        Plan saved = repository.save(request.toPlan(loginUser.getId(), planThemes));

        return PlanResponse.of(saved);
    }

    public PlanResponse updatePlace(LoginUser loginUser, Long id, Long placeId) {
        Plan plan = findById(id);

        plan.canEdit(loginUser.getId());

        plan.updatePlace(placeId);

        return PlanResponse.of(plan);
    }

    @Transactional(readOnly = true)
    public PlanResponse findResponseById(LoginUser loginUser, Long id) {
        Plan plan = findById(id);

        plan.canRead(loginUser.getId());

        return PlanResponse.of(plan);
    }

    @Transactional(readOnly = true)
    public Plan findCanReadPlanById(LoginUser loginUser, Long id) {
        Plan plan = findById(id);

        plan.canRead(loginUser.getId());

        return plan;
    }

    private Plan findById(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 일정을 찾을 수 없습니다."));
    }
}
