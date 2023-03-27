package com.avatar.trip.plan.schedule.domain;

import com.avatar.trip.plan.common.domain.Amount;
import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.common.domain.Note;
import com.avatar.trip.plan.common.domain.SortSeq;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.exception.WrongDateException;
import com.avatar.trip.plan.plan.domain.Plan;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
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
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Days day;

    private Long placeId;

    @Embedded
    private SortSeq order;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "budget"))
    private Amount budget;

    @Embedded
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(name = "fk_schedule_plan"))
    private Plan plan;

    private Schedule(Integer day, Long placeId, Integer order, Plan mainPlan) {
        validate(day, placeId, order, mainPlan);
        this.day = Days.valueOf(day);
        this.placeId = placeId;
        this.order = SortSeq.valueOf(order);
        setPlan(mainPlan);
    }

    public static Schedule of(Integer day, Long placeId, Integer order, Plan mainPlan) {
        return new Schedule(day, placeId, order, mainPlan);
    }

    public void inputBudget(BigDecimal budget){
        this.budget = Amount.valueOf(budget);
    }

    public void canRead(Long userId){
        this.plan.canRead(userId);
    }

    public void canEdit(Long userId){
        this.plan.canEdit(userId);
    }

    public void takeNotes(String note){
        this.note = Note.valueOf(note);
    }

    public String toStringNote() {
        return this.note.toString();
    }

    public int toIntDay(){
        return this.day.getDays();
    }

    public int toIntOrder(){
        return this.order.getSort();
    }

    private void validate(Integer day, Long placeId, Integer order, Plan mainPlan){
        if (day == null) {
            throw new RequiredArgumentException("일차");
        }

        if (placeId == null) {
            throw new RequiredArgumentException("장소");
        }

        if(order == null){
            throw new RequiredArgumentException("순서");
        }

        if(mainPlan == null){
            throw new RequiredArgumentException("메인 일정");
        }

        validateDay(day, mainPlan);
    }

    private void validateDay(Integer day, Plan mainPlan){
        if (!mainPlan.containDays(day)){
            throw new WrongDateException(String.format("일차는 여행기간 내로 입력하세요. (여행기간: %s 일)", mainPlan.getDays()));
        }
    }

    private void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        return id.equals(schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + id +
            ", day=" + day +
            ", placeId=" + placeId +
            ", order=" + order +
            ", budget=" + budget +
            ", note=" + note +
            ", plan=" + plan.getId() +
            '}';
    }
}
