package com.avatar.trip.plan.user.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.common.domain.Role;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    @Enumerated(EnumType.STRING)
    private Role role;

    protected Authority(){
    }

    private Authority(Role role){
        this.role = role;
    }

    public static Authority from(Role role){
        return new Authority(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Authority authority = (Authority) o;
        return id.equals(authority.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
