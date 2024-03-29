package com.avatar.trip.plan.user.domain;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import java.util.Objects;
import javax.persistence.Entity;
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
public class UserAuthority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "authority_id")
    private Authority authority;

    private UserAuthority(Authority authority){
        validate(authority);
        this.authority = authority;
    }

    private void validate(Authority authority){
        if (authority == null){
            throw new RequiredArgumentException("권한");
        }
    }

    public static UserAuthority of( Authority authority){
        return new UserAuthority(authority);
    }

    public boolean equalsUser(User user) {
        if (this.user == null){
            return false;
        }
        return this.user.equals(user);
    }

    public boolean equalsAuthority(Authority authority) {
        return this.authority.equals(authority);
    }

    public boolean equalRole(Role role){
        return this.authority.equalRole(role);
    }

    public void removeUser() {
        if(this.user == null){
            return;
        }

        this.user.removeAuthority(this);
        this.user = null;
    }

    public void setUser(User user){
        if(this.user != null){
            this.user.removeAuthority(this);
        }
        this.user = user;
        if(!user.containAuthority(this)){
            user.addAuthority(this);
        }
    }

    public String getAuthorityName() {
        return this.authority.getRoleId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAuthority that = (UserAuthority) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserAuthority{" +
            "id=" + id +
            ", user=" + user.getEmail() +
            ", authority=" + authority.getName() +
            '}';
    }
}
