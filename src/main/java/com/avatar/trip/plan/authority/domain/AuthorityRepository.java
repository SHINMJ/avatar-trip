package com.avatar.trip.plan.user.domain;

import com.avatar.trip.plan.common.domain.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByRole(Role role);
}
