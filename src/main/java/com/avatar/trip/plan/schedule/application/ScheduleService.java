package com.avatar.trip.plan.schedule.application;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.BusinessException;
import com.avatar.trip.plan.exception.CannotDeleteException;
import com.avatar.trip.plan.exception.NotFoundException;
import com.avatar.trip.plan.plan.application.PlanService;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleRepository;
import com.avatar.trip.plan.schedule.domain.Schedules;
import com.avatar.trip.plan.schedule.dto.ScheduleBudgetRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleNoteRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.math.BigDecimal;
import java.util.List;
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
        Plan plan = planService.findCanReadPlanById(loginUser, request.getPlanId());

        plan.canEdit(loginUser.getId());

        Schedule saved = repository.save(request.toSchedule(plan));

        return ScheduleResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllByPlan(LoginUser loginUser, Long planId) {
        Plan plan = planService.findCanReadPlanById(loginUser, planId);

        List<Schedule> schedules = repository
            .findAllByPlanOrderByDayAscOrderAsc(plan);
        return ScheduleResponse.ofList(schedules);
    }

    public ScheduleResponse inputBudget(LoginUser loginUser, Long id, ScheduleBudgetRequest request) {
        Schedule schedule = findEditScheduleById(loginUser, id);
        schedule.inputBudget(request.getBudget());

        return ScheduleResponse.of(schedule);
    }

    public ScheduleResponse takeNotes(LoginUser loginUser, Long id, ScheduleNoteRequest request) {
        Schedule schedule = findEditScheduleById(loginUser, id);
        schedule.takeNotes(request.getNote());

        return ScheduleResponse.of(schedule);
    }

    public void delete(LoginUser loginUser, Long id) {
        try {
            Schedule schedule = findEditScheduleById(loginUser, id);
            repository.delete(schedule);

            rearrange(schedule.getPlan(), schedule.getDay());

        }catch (BusinessException e) {
            throw new CannotDeleteException(e.getMessage());
        }catch (Exception e) {
            throw new CannotDeleteException();
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal findTotalBudgetByDay(LoginUser loginUser, Long planId, int day) {
        Plan plan = planService.findCanReadPlanById(loginUser, planId);

        List<Schedule> schedules = repository
            .findByPlanAndDayOrderByOrder(plan, Days.valueOf(day));

        return Schedules.of(schedules).totalBudget();
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findResponseById(LoginUser loginUser, Long id) {
        Schedule schedule = findById(id);
        schedule.canRead(loginUser.getId());
        return ScheduleResponse.of(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllByPlanAndDay(LoginUser loginUser, Long planId, int day) {
        Plan plan = planService.findCanReadPlanById(loginUser, planId);
        List<Schedule> schedules = repository.findByPlanAndDayOrderByOrder(plan, Days.valueOf(day));
        return ScheduleResponse.ofList(schedules);
    }

    private void rearrange(Plan plan, Days days) {
        List<Schedule> schedules = repository
            .findByPlanAndDayOrderByOrder(plan, days);

        Schedules rearrange = Schedules.of(schedules).rearrange();

        repository.saveAll(rearrange.getScheduleList());
    }

    private Schedule findEditScheduleById(LoginUser loginUser, Long id){
        Schedule schedule = findById(id);
        schedule.canEdit(loginUser.getId());
        return schedule;
    }

    private Schedule findById(Long id){
        return repository.findById(id)
            .orElseThrow(NotFoundException::new);
    }

}
