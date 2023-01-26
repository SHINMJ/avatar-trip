package com.avatar.trip.plan.user.domain;

import com.avatar.trip.plan.common.domain.BaseEntity;
import com.avatar.trip.plan.exception.RequiredArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Entity(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    private String nickname;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAuthority> userAuthorities = new ArrayList<>();

    protected User(){}

    private User(String email, String password) {
        validate(email, password);
        this.email = email;
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

    public void updatePassword(String password) {
        this.password = password;
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
