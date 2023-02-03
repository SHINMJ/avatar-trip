package com.avatar.trip.plan.authority.domain;

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
import lombok.ToString;

@ToString
@Getter
@Entity
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "authority_name")
    private String name;

    protected Authority(){
    }

    private Authority(Role role, String name){
        this.role = role;
        this.name = name;
    }

    public static Authority from(Role role){
        return new Authority(role, role.getTitle());
    }

    public static Authority of(Role role, String name){
        return new Authority(role, name);
    }

    public String getRoleId(){
        return this.role.getKey();
    }

    public boolean equalRole(Role role){
        return this.role.equals(role);
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
