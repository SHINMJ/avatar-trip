package com.avatar.trip.plan.user.domain;

import com.avatar.trip.plan.exception.RequiredArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAuthority> userAuthorities = new ArrayList<>();

    protected User(){}

    private User(String username, String password) {
        validate(username, password);
        this.username = username;
        this.password = password;
    }

    private void validate(String username, String password){
        if (!StringUtils.hasLength(username)){
            throw new RequiredArgumentException("이름");
        }

        if (!StringUtils.hasLength(password)) {
            throw new RequiredArgumentException("비밀번호");
        }

    }

    public static User of(String username, String password){
        return new User(username, password);
    }

    public void removeAuthority(UserAuthority userAuthority) {
        this.userAuthorities.remove(userAuthority);
        if(userAuthority.equalsUser(this)){
            userAuthority.removeUser();
        }
    }

    public boolean containAuthority(UserAuthority userAuthority) {
        return this.userAuthorities.contains(userAuthority);
    }

    public void addAuthority(UserAuthority userAuthority) {
        this.userAuthorities.add(userAuthority);
        if(!userAuthority.equalsUser(this)){
            userAuthority.setUser(this);
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
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
