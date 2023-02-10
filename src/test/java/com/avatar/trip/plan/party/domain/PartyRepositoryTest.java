package com.avatar.trip.plan.party.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PartyRepositoryTest {

    @Autowired
    private PartyRepository partyRepository;

    @BeforeEach
    void setUp() {
        Party party1 = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, 1L);
        Party party2 = Party.of(PhoneNumber.valueOf("010-0000-1233"), Permission.READ, 1L);
        Party party3 = Party.of(PhoneNumber.valueOf("010-0000-1235"), Permission.READ, 2L);

        partyRepository.saveAll(List.of(party1, party2, party3));
    }

    @AfterEach
    void tearDown() {
        partyRepository.deleteAll();
        partyRepository.flush();
    }

    @Test
    @DisplayName("일행 생성")
    void created() {
        Party party = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, 1L);
        partyRepository.save(party);

        partyRepository.flush();
    }

    @Test
    @DisplayName("일행 여러명 생성")
    void created_withList() {
        Party party1 = Party.of(PhoneNumber.valueOf("010-0000-1234"), Permission.EDIT, 1L);
        Party party2 = Party.of(PhoneNumber.valueOf("010-0000-1233"), Permission.READ, 1L);
        Party party3 = Party.of(PhoneNumber.valueOf("010-0000-1235"), Permission.READ, 1L);

        partyRepository.saveAll(List.of(party1, party2, party3));

        partyRepository.flush();
    }

    @Test
    @DisplayName("스케쥴에 해당하는 일행 모두 찾기")
    void findBySchedule() {
        List<Party> parties = partyRepository.findAllByScheduleId(1L);

        assertThat(parties.size()).isEqualTo(2);
    }
}