package com.avatar.trip.plan.party.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/partyTestSetup.sql")
class PartyRepositoryTest {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PlanRepository planRepository;

    private Plan plan1;
    private Plan plan2;

    @BeforeEach
    void setUp() {
        plan1 = planRepository.findById(1L).get();
        plan2 = planRepository.findById(2L).get();

        Party party1 = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, plan1);
        Party party2 = Party.of(PhoneNumber.valueOf("010-0000-1233"), Permission.READ, plan1);
        Party party3 = Party.of(PhoneNumber.valueOf("010-0000-1235"), Permission.READ, plan2);

        partyRepository.saveAll(List.of(party1, party2, party3));
    }

    @Test
    @DisplayName("일행 생성")
    void created() {
        Party party = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, plan1);
        partyRepository.save(party);

        partyRepository.flush();
    }

    @Test
    @DisplayName("일행 여러명 생성")
    void created_withList() {
        Party party1 = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, plan1);
        Party party2 = Party.of(PhoneNumber.valueOf("010-0000-1233"), Permission.READ, plan1);
        Party party3 = Party.of(PhoneNumber.valueOf("010-0000-1235"), Permission.READ, plan1);

        partyRepository.saveAll(List.of(party1, party2, party3));

        partyRepository.flush();
    }

    @Test
    @DisplayName("스케쥴에 해당하는 일행 모두 찾기")
    void findBySchedule() {
        List<Party> parties = partyRepository.findAllByPlan(plan1);

        assertThat(parties.size()).isEqualTo(2);
    }
}