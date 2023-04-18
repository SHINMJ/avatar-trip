package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.SortSeq;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.ToString;

public class Schedules {

    private final List<Schedule> scheduleList;

    private Schedules(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public static Schedules of(List<Schedule> schedules){
        List<Schedule> list = new ArrayList<>(schedules);
        return new Schedules(list);
    }

    public List<Schedule> getScheduleList(){
        return this.scheduleList;
    }

    public BigDecimal totalBudget() {
        return this.scheduleList.stream()
            .map(Schedule::toDecimalBudget)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Schedules rearrange() {

        Collections.sort(this.scheduleList, Comparator.comparing(Schedule::getOrder));

        for (int i = 0; i < scheduleList.size(); i++) {
            Schedule schedule = scheduleList.get(i);
            SortSeq sortSeq = SortSeq.valueOf(i + 1);
            if (schedule.equalsOrder(sortSeq)){
                continue;
            }
            schedule.modifyOrder(sortSeq);
        }

        return Schedules.of(this.scheduleList);
    }

    @Override
    public String toString() {
        return scheduleList.toString();
    }
}
