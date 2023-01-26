package com.avatar.trip.plan.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/setup.sql")
class UserRepositoryTest {
    static final User TEST_USER = User.of("테스트사용자", "1234@");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private Authority roleAdmin;
    private Authority roleUser;

    @BeforeEach
    void setUp() {
        roleAdmin = authorityRepository.findById(1L).get();
        roleUser = authorityRepository.findById(2L).get();
    }

    @Test
    void createUser() {
        User saved = userRepository.save(TEST_USER);

        //쿼리 확인
        userRepository.flush();

        Optional<User> optionalUser = userRepository.findById(saved.getId());
        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();
        assertTrue(user.equals(saved));
    }

    @Test
    void updateUserInfo() {
        User saved = userRepository.save(TEST_USER);

        assertThat(saved.getPassword()).isEqualTo(TEST_USER.getPassword());

        saved.updatePassword("11111");

        //쿼리 확인
        userRepository.flush();

        assertThat(saved.getPassword()).isEqualTo("11111");
    }

    @Test
    void createUserWithAuthority() {
        assertThat(TEST_USER.getUserAuthorities().size()).isEqualTo(0);

        TEST_USER.addAuthority(UserAuthority.of(roleAdmin));
        TEST_USER.addAuthority(UserAuthority.of(roleUser));

        User saved = userRepository.save(TEST_USER);

        userRepository.flush();

        assertThat(TEST_USER.getUserAuthorities().size()).isEqualTo(2);
    }
}