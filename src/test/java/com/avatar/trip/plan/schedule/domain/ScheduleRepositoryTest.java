package com.avatar.trip.plan.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/scheduleTestSetup.sql")
class ScheduleRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Plan mainPlan;

    @BeforeEach
    void setUp() {
        mainPlan = planRepository.findById(1L).get();
    }

    @Test
    void createSchedule() {
        Schedule schedule = Schedule.of(1, 1L, 1, mainPlan);

        scheduleRepository.save(schedule);

        scheduleRepository.flush();
    }

    @Test
    void findSchedulesByPlanIdAndDays() {
        Schedule schedule1 = Schedule.of(1, 1L, 1, mainPlan);
        Schedule schedule2 = Schedule.of(1, 1L, 2, mainPlan);
        Schedule schedule3 = Schedule.of(2, 1L, 1, mainPlan);
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        List<Schedule> schedules = scheduleRepository
            .findByPlanAndDayOrderByOrder(mainPlan, Days.valueOf(1));

        assertThat(schedules.size()).isEqualTo(2);
        assertThat(schedules).allSatisfy(schedule -> {
            assertThat(schedule.getDay()).isEqualTo(Days.valueOf(1));
            assertThat(schedule.getPlan()).isEqualTo(mainPlan);
        });
    }

    @Test
    void findById() {
        Schedule schedule = Schedule.of(1, 1L, 1, mainPlan);
        Schedule save = scheduleRepository.save(schedule);

        Optional<Schedule> optionalSchedule = scheduleRepository.findById(save.getId());

        assertTrue(optionalSchedule.isPresent());
        Schedule find = optionalSchedule.get();
        assertThat(find).isEqualTo(save);
    }

    @Test
    void findAllByPlanId() {
        Schedule schedule1 = Schedule.of(1, 1L, 1, mainPlan);
        Schedule schedule2 = Schedule.of(1, 1L, 2, mainPlan);
        Schedule schedule3 = Schedule.of(2, 1L, 1, mainPlan);
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        List<Schedule> schedules = scheduleRepository.findAllByPlanOrderByDayAscOrderAsc(mainPlan);
        assertThat(schedules.size()).isEqualTo(3);
        assertThat(schedules).allSatisfy(schedule -> {
            assertThat(schedule.getPlan()).isEqualTo(mainPlan);
        });

    }

}