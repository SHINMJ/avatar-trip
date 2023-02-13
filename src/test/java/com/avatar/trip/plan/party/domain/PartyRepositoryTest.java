package com.avatar.trip.plan.party.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avatar.trip.plan.schedule.domain.Schedule;
import com.avatar.trip.plan.schedule.domain.ScheduleRepository;
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
    private ScheduleRepository scheduleRepository;

    private Schedule schedule1;
    private Schedule schedule2;

    @BeforeEach
    void setUp() {
        schedule1 = scheduleRepository.findById(1L).get();
        schedule2 = scheduleRepository.findById(2L).get();

        Party party1 = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, schedule1);
        Party party2 = Party.of(PhoneNumber.valueOf("010-0000-1233"), Permission.READ, schedule1);
        Party party3 = Party.of(PhoneNumber.valueOf("010-0000-1235"), Permission.READ, schedule2);

        partyRepository.saveAll(List.of(party1, party2, party3));
    }

    @Test
    @DisplayName("일행 생성")
    void created() {
        Party party = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, schedule1);
        partyRepository.save(party);

        partyRepository.flush();
    }

    @Test
    @DisplayName("일행 여러명 생성")
    void created_withList() {
        Party party1 = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, schedule1);
        Party party2 = Party.of(PhoneNumber.valueOf("010-0000-1233"), Permission.READ, schedule1);
        Party party3 = Party.of(PhoneNumber.valueOf("010-0000-1235"), Permission.READ, schedule1);

        partyRepository.saveAll(List.of(party1, party2, party3));

        partyRepository.flush();
    }

    @Test
    @DisplayName("스케쥴에 해당하는 일행 모두 찾기")
    void findBySchedule() {
        List<Party> parties = partyRepository.findAllBySchedule(schedule1);

        assertThat(parties.size()).isEqualTo(2);
    }
}