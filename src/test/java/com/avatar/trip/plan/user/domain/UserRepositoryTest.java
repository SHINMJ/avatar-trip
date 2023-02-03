package com.avatar.trip.plan.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.authority.domain.AuthorityRepository;
import com.avatar.trip.plan.common.domain.Role;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/userTestSetup.sql")
class UserRepositoryTest {
    static final String EMAIL = "user@email.com";
    static final String PASSWORD = "1111";
    static final String NICKNAME = "test";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private Authority roleAdmin;
    private Authority roleUser;

    @BeforeEach
    void setUp() {
        roleAdmin = authorityRepository.findByRole(Role.ADMIN).get();
        roleUser = authorityRepository.findByRole(Role.USER).get();
    }

    @Test
    void createUser() {
        User saved = userRepository.save(User.of(EMAIL, PASSWORD));

        //쿼리 확인
        userRepository.flush();

        Optional<User> optionalUser = userRepository.findById(saved.getId());
        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();
        assertTrue(user.equals(saved));
    }

    @Test
    void updateUserInfo() {
        User saved = userRepository.save(User.of(EMAIL, PASSWORD));

        assertThat(saved.getPassword()).isEqualTo(PASSWORD);

        saved.updateInfo( "11111", "test");

        //쿼리 확인
        userRepository.flush();

        assertThat(saved.getPassword()).isNotEqualTo(PASSWORD);
    }

    @Test
    void createUserWithAuthority() {
        User user = User.of(EMAIL, PASSWORD, NICKNAME, List.of(UserAuthority.of(roleAdmin), UserAuthority.of(roleUser)));

        User saved = userRepository.save(user);

        userRepository.flush();

        assertThat(saved.getUserAuthorities().size()).isEqualTo(2);
    }

    @Test
    void findByEmail() {
        User saved = userRepository.save(User.of(EMAIL, PASSWORD));

        Optional<User> byEmail = userRepository.findByEmail(EMAIL);

        assertTrue(byEmail.isPresent());
    }
}