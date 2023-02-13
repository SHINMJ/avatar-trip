package com.avatar.trip.plan.schedule.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.theme.domain.ThemeRepository;
import java.util.List;
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
        theme = themeRepository.findById(1L).get();
    }

    @Test
    void created() {
        Schedule schedule = Schedule.of(1L, 1L, List.of(ScheduleTheme.of(theme)),
            Period.of(Days.valueOf(1), Days.valueOf(2)));

        scheduleRepository.save(schedule);

        scheduleRepository.flush();
    }
}