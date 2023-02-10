package com.avatar.trip.plan.party.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {

    List<Party> findAllByScheduleId(Long scheduleId);
}
