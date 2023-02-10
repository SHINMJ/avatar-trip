package com.avatar.trip.plan.party.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.schedule.domain.Schedule;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    private Long scheduleId;

    private Boolean send = Boolean.FALSE;

    private Party(PhoneNumber phoneNumber, Permission permission, Long scheduleId) {
        validate(phoneNumber, permission, scheduleId);
        this.phoneNumber = phoneNumber;
        this.permission = permission;
        this.scheduleId = scheduleId;
    }

    public static Party of(PhoneNumber phoneNumber, Permission permission, Long scheduleId){
        return new Party(phoneNumber, permission, scheduleId);
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void sendSMS(){
        this.send = Boolean.TRUE;
    }

    private void validate(PhoneNumber phoneNumber, Permission permission, Long scheduleId) {
        if (phoneNumber == null){
            throw new RequiredArgumentException("휴대폰 번호");
        }

        if (permission == null){
            throw new RequiredArgumentException("권한레벨");
        }

        if(scheduleId == null){
            throw new RequiredArgumentException("일정");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Party party = (Party) o;
        return id.equals(party.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
