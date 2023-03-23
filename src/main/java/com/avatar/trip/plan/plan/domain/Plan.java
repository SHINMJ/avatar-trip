package com.avatar.trip.plan.plan.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.common.domain.Days;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.exception.UnauthorizedException;
import com.avatar.trip.plan.party.domain.Parties;
import com.avatar.trip.plan.party.domain.Party;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Period period;

    @Embedded
    private PeriodDate periodDate;

    private Long placeId;

    @Embedded
    private PlanThemes themes = new PlanThemes();

    private Long ownerId;

    @Embedded
    private Parties parties = new Parties();

    private Plan(Long ownerId, Long placeId, List<PlanTheme> themes, Period period) {
        validate(ownerId, period, placeId, themes);
        this.ownerId = ownerId;
        this.placeId = placeId;
        setThemes(themes);
        this.period = period;
    }

    private Plan(Long ownerId, Long placeId, List<PlanTheme> themes, Period period, PeriodDate periodDate) {
        validate(ownerId, period, placeId, themes);
        this.ownerId = ownerId;
        this.placeId = placeId;
        setThemes(themes);
        this.period = period;
        this.periodDate = periodDate;
    }


    public static Plan of(Long ownerId, Long placeId, List<PlanTheme> themes, Period period) {
        return new Plan(ownerId, placeId, themes, period);
    }

    public static Plan ofDate(Long ownerId, Long placeId, List<PlanTheme> themes, PeriodDate periodDate) {
        validatePeriodDate(periodDate);
        return new Plan(ownerId, placeId, themes, periodDate.toPeriod(), periodDate);
    }

    public void addTheme(PlanTheme theme) {
        themes.addTheme(theme);
        if(!theme.equalPlan(this)){
            theme.setPlan(this);
        }
    }

    public boolean containTheme(PlanTheme theme) {
        return this.themes.contains(theme);
    }

    public void removeTheme(PlanTheme theme) {
        this.themes.remove(theme);
        if(theme.equalPlan(this)){
            theme.removePlan();
        }
    }

    public void addParty(Party party){
        this.parties.addParty(party);
        if (!party.equalPlan(this)){
            party.setPlan(this);
        }
    }

    public boolean containParty(Party party){
        return this.parties.contains(party);
    }

    public void removeParty(Party party){
        this.parties.removeParty(party);
        if (party.equalPlan(this)) {
            party.removePlan();
        }
    }

    public void canEdit(Long userId) {
        if (!(isOwner(userId) || this.parties.canEdit(userId))){
            throw new UnauthorizedException("권한이 없어 수정할 수 없습니다.");
        }
    }

    public boolean canRead(Long userId) {
        if (!(isOwner(userId) || this.parties.canRead(userId))){
            throw new UnauthorizedException("권한이 없어 일정을 볼 수 없습니다.");
        }
        return false;
    }

    public List<String> getThemeNames(){
        return this.themes.getThemeNames();
    }

    public String getPeriodString(){
        return this.period.toString();
    }

    public void updatePlace(Long placeId) {
        this.placeId = placeId;
    }

    public boolean containDays(Integer days) {
        return this.period.contains(days);
    }

    public Days getDays(){
        return this.period.getDay();
    }

    private static void validatePeriodDate(PeriodDate periodDate) {
        if(periodDate == null){
            throw new RequiredArgumentException("날짜");
        }
    }

    private void validate(Long ownerId, Period period, Long placeId, List<PlanTheme> themes) {
        if (ownerId == null){
            throw new RequiredArgumentException("사용자");
        }

        if (period == null){
            throw new RequiredArgumentException("기간");
        }

        if(placeId == null){
            throw new RequiredArgumentException("지역");
        }

        if(themes == null || themes.size() <= 0){
            throw new RequiredArgumentException("테마");
        }
    }


    private void setThemes(List<PlanTheme> themes) {
        for (PlanTheme theme: themes) {
            addTheme(theme);
        }
    }

    private boolean isOwner(Long userId){
        return this.ownerId.equals(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Plan plan = (Plan) o;
        return id.equals(plan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
