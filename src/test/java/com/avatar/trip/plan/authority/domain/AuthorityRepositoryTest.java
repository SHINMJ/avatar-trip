package com.avatar.trip.plan.authority.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Role;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    void createAuthority() {
        Authority admin = Authority.from(Role.ADMIN);
        Authority save = authorityRepository.save(admin);

        //쿼리 확인
        authorityRepository.flush();

        Optional<Authority> optional = authorityRepository.findById(save.getId());

        assertTrue(optional.isPresent());
        Authority findAdmin = optional.get();

        assertTrue(findAdmin.equals(save));
    }

    @Test
    void findByRole() {
        authorityRepository.save(Authority.from(Role.ADMIN));
        authorityRepository.save(Authority.from(Role.USER));

        Optional<Authority> byRole = authorityRepository.findByRole(Role.USER);

        assertTrue(byRole.isPresent());

        Authority authority = byRole.get();

        assertThat(authority.getRole()).isEqualTo(Role.USER);
    }
}