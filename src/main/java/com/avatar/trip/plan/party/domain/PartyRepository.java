package com.avatar.trip.plan.party.domain;

import com.avatar.trip.plan.schedule.domain.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {

    List<Party> findAllBySchedule(Schedule schedule);
}
