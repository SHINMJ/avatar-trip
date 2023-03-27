package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.plan.domain.Plan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByPlanAndDayOrderByOrder(Plan plan, Days days);
    List<Schedule> findAllByPlanOrderByDayAscOrderAsc(Plan plan);
}
