package com.avatar.trip.plan.plan.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.theme.domain.Theme;
import java.util.Objects;
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
public class PlanTheme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(name = "fk_plan_theme_plan"))
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", foreignKey = @ForeignKey(name = "fk_plan_theme_theme"))
    private Theme theme;

    private PlanTheme(Theme theme){
        validate(theme);
        this.theme = theme;
    }

    private void validate(Theme theme) {
        if (theme == null){
            throw new RequiredArgumentException("테마");
        }
    }

    public static PlanTheme of(Theme theme){
        return new PlanTheme(theme);
    }

    public boolean equalPlan(Plan plan) {
        if(this.plan == null){
            return false;
        }
        return this.plan.equals(plan);
    }


    public void setPlan(Plan plan) {
        if(this.plan != null){
            this.plan.removeTheme(this);
        }

        this.plan = plan;

        if(!this.plan.containTheme(this)){
            this.plan.addTheme(this);
        }
    }

    public void removePlan() {
        if(this.plan == null){
            return;
        }
        this.plan.removeTheme(this);
        this.plan = null;
    }

    public String getThemeName(){
        return theme.getThemeName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlanTheme that = (PlanTheme) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
