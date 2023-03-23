package com.avatar.trip.plan.plan.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.theme.domain.ThemeRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/planTestSetup.sql")
class PlanRepositoryTest {


    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ThemeRepository themeRepository;

    private Theme theme;

    @BeforeEach
    void setUp() {
        theme = themeRepository.findAll().get(0);
    }

    @Test
    void created() {
        Plan plan = Plan.of(1L, 1L, List.of(PlanTheme.of(theme)),
            Period.of(1,2));

        planRepository.save(plan);

        planRepository.flush();
    }

    @Test
    void update() {
        Plan plan = Plan.of(1L, 1L, List.of(PlanTheme.of(theme)),
            Period.of(1,2));

        Plan saved = planRepository.save(plan);

        planRepository.flush();

        assertThat(saved.getPlaceId()).isEqualTo(1L);

        saved.updatePlace(2L);

        Plan findPlan = planRepository.findById(saved.getId()).get();

        //쿼리 확인
        planRepository.flush();

        assertAll(
            () -> assertThat(findPlan.getPlaceId()).isEqualTo(2L),
            () -> assertThat(saved.getPlaceId()).isEqualTo(2L),
            () -> assertTrue(findPlan.equals(saved))
        );


    }
}