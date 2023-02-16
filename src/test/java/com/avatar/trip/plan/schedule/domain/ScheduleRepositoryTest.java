package com.avatar.trip.plan.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.theme.domain.ThemeRepository;
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
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ThemeRepository themeRepository;

    private Theme theme;

    @BeforeEach
    void setUp() {
        theme = themeRepository.findAll().get(0);
    }

    @Test
    void created() {
        Schedule schedule = Schedule.of(1L, 1L, List.of(ScheduleTheme.of(theme)),
            Period.of(1,2));

        scheduleRepository.save(schedule);

        scheduleRepository.flush();
    }

    @Test
    void update() {
        Schedule schedule = Schedule.of(1L, 1L, List.of(ScheduleTheme.of(theme)),
            Period.of(1,2));

        Schedule saved = scheduleRepository.save(schedule);

        scheduleRepository.flush();

        assertThat(saved.getPlaceId()).isEqualTo(1L);

        saved.updatePlace(2L);

        Schedule findSchedule = scheduleRepository.findById(saved.getId()).get();

        //쿼리 확인
        scheduleRepository.flush();

        assertAll(
            () -> assertThat(findSchedule.getPlaceId()).isEqualTo(2L),
            () -> assertThat(saved.getPlaceId()).isEqualTo(2L),
            () -> assertTrue(findSchedule.equals(saved))
        );


    }
}