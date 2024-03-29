package com.avatar.trip.plan.authority.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.common.domain.Role;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

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

    @Test
    void findSlice() {
        Authority admin = authorityRepository.save(Authority.from(Role.ADMIN));
        Authority user = authorityRepository.save(Authority.from(Role.USER));

        authorityRepository.flush();

        Slice<Authority> sliceAll = authorityRepository.findAllBy(PageRequest.of(0, 10));

        System.out.println(sliceAll.getContent());
        assertTrue(sliceAll.hasContent());
        assertAll(
            () -> assertThat(sliceAll.getNumberOfElements()).isEqualTo(2),
            () -> assertThat(sliceAll.getContent()).contains(admin, user)
        );
    }
}