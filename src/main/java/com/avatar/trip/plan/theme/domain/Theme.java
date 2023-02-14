package com.avatar.trip.plan.theme.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.exception.CannotDeleteException;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import java.util.Objects;
import javax.persistence.Column;
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
public class Theme extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ThemeName name;

    private Long ownerId;
    private Boolean isAdmin = false;

    private Theme(String name, Long ownerId, Boolean isAdmin){
        validate(ownerId);
        this.name = ThemeName.valueOf(name);
        this.ownerId = ownerId;
        this.isAdmin = isAdmin;
    }

    private void validate(Long ownerId) {
        if(ownerId == null){
            throw new RequiredArgumentException("사용자는 필수 입니다.");
        }
    }

    public static Theme of(String name, Long ownerId){
        return new Theme(name, ownerId, Boolean.FALSE);
    }

    public static Theme ofAdmin(String name, Long ownerId){
        return new Theme(name, ownerId, Boolean.TRUE);
    }

    public boolean isOwner(Long ownerId) {
        return this.ownerId.equals(ownerId);
    }

    public void validateDelete(Long ownerId) {
        if(!isOwner(ownerId)){
            throw new CannotDeleteException("다른 사용자의 테마를 사용할 수 없습니다.");
        }
    }

    public String getThemeName(){
        return this.name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return id.equals(theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
