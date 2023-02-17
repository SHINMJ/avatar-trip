package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.Amount;
import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.common.domain.Note;
import com.avatar.trip.plan.common.domain.SortSeq;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.exception.WrongDateException;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Days day;

    private Long placeId;

    @Embedded
    private SortSeq order;

    @Embedded
    private Amount budget;

    @Embedded
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", foreignKey = @ForeignKey(name = "fk_sub_schedule"))
    private Schedule schedule;

    private SubSchedule(Integer day, Long placeId, Integer order, Schedule mainSchedule) {
        validate(day, placeId, order, mainSchedule);
        this.day = Days.valueOf(day);
        this.placeId = placeId;
        this.order = SortSeq.valueOf(order);
        setSchedule(mainSchedule);
    }

    public static SubSchedule of(Integer day, Long placeId, Integer order, Schedule mainSchedule) {
        return new SubSchedule(day, placeId, order, mainSchedule);
    }

    public void inputBudget(BigDecimal budget){
        this.budget = Amount.valueOf(budget);
    }

    public void canRead(Long userId){
        this.schedule.canRead(userId);
    }

    public void canEdit(Long userId){
        this.schedule.canEdit(userId);
    }

    public void takeNote(String note){
        this.note = Note.valueOf(note);
    }

    public String toStringNote() {
        return this.note.toString();
    }

    private void validate(Integer day, Long placeId, Integer order, Schedule mainSchedule){
        if (day == null) {
            throw new RequiredArgumentException("일차");
        }

        if (placeId == null) {
            throw new RequiredArgumentException("장소");
        }

        if(order == null){
            throw new RequiredArgumentException("순서");
        }

        if(mainSchedule == null){
            throw new RequiredArgumentException("메인 일정");
        }

        validateDay(day, mainSchedule);
    }

    private void validateDay(Integer day, Schedule mainSchedule){
        if (!mainSchedule.containDays(day)){
            throw new WrongDateException(String.format("일차는 여행기간 내로 입력하세요. (여행기간: %s 일)", mainSchedule.getDays()));
        }
    }

    private void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubSchedule schedule = (SubSchedule) o;
        return id.equals(schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
