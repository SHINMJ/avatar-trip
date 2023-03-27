package com.avatar.trip.plan.schedule.application;

import com.avatar.trip.plan.plan.application.PlanService;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleRepository;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
import com.avatar.trip.plan.user.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository repository;
    private final PlanService planService;


    public ScheduleResponse create(LoginUser loginUser, ScheduleRequest request) {
        Plan plan = planService.findPlanById(loginUser, request.getPlanId());

        plan.canEdit(loginUser.getId());

        Schedule saved = repository.save(request.toSchedule(plan));

        return ScheduleResponse.of(saved);
    }
}
