package com.avatar.trip.plan.authority.domain;

import com.avatar.trip.plan.common.domain.Role;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByRole(Role role);
    Slice<Authority> findAllBy(Pageable pageable);
}
