package com.avatar.trip.plan.theme.domain;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Page<Theme> findAllBy(Pageable pageable);
    Slice<Theme> findAllByOwnerIdOrIsAdminTrue(Long ownerId, Pageable pageable);
    List<Theme> findByIdIn(List<Long> ids);
}
