package com.avatar.trip.plan.authority.application;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.authority.domain.AuthorityRepository;
import com.avatar.trip.plan.authority.dto.AuthorityRequest;
import com.avatar.trip.plan.authority.dto.AuthorityResponse;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.exception.NotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthorityService {

    private final AuthorityRepository repository;

    public AuthorityResponse createAuthority(AuthorityRequest request){
        Authority saved = repository.save(request.toAuthority());
        return AuthorityResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public Slice<AuthorityResponse> findAll(Pageable pageable) {
        Slice<Authority> sliceAll = repository.findAllBy(pageable);
        return sliceAll.map(AuthorityResponse::of);
    }

    @Transactional(readOnly = true)
    public AuthorityResponse findByRoleId(String roleId) {
        Authority authority = findByRole(Role.findByKey(roleId));
        return AuthorityResponse.of(authority);
    }

    public Authority findByRole(Role role){
        return repository.findByRole(role)
            .orElseThrow(() -> new NotFoundException("해당 권한을 찾을 수 없습니다."));
    }

    public void delete(Long id) {
        Authority authority = findById(id);
        repository.delete(authority);
    }

    private Authority findById(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 권한을 찾을 수 없습니다."));
    }

}
