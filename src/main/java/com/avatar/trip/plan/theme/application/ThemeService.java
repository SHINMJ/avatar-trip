package com.avatar.trip.plan.theme.application;

import com.avatar.trip.plan.exception.NotFoundException;
import com.avatar.trip.plan.exception.UnauthorizedException;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.theme.domain.ThemeRepository;
import com.avatar.trip.plan.theme.dto.ThemeRequest;
import com.avatar.trip.plan.theme.dto.ThemeResponse;
import com.avatar.trip.plan.user.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ThemeService {
    private final ThemeRepository repository;

    public ThemeResponse create(ThemeRequest request, LoginUser loginUser) {
        Theme saved = repository.save(request.toTheme(loginUser.getId()));

        return ThemeResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public Slice<ThemeResponse> findAllByUser(LoginUser loginUser, Pageable pageable) {
        Slice<Theme> themes = repository
            .findAllByOwnerIdOrIsAdminTrue(loginUser.getId(), pageable);

        return themes.map(ThemeResponse::of);
    }

    @Transactional(readOnly = true)
    public Page<ThemeResponse> findAll(LoginUser loginUser, Pageable pageable) {
        if (!loginUser.isAdmin()){
            throw new UnauthorizedException();
        }
        Page<Theme> themes = repository.findAllBy(pageable);
        return themes.map(ThemeResponse::of);

    }

    @Transactional(readOnly = true)
    public ThemeResponse findResponseById(Long id, LoginUser loginUser) {
        Theme theme = findById(id);

        if(!loginUser.isAdmin() && !theme.isOwner(loginUser.getId())){
            throw new NotFoundException();
        }

        return ThemeResponse.of(theme);
    }

    public void delete(Long id, LoginUser loginUser) {
        Theme theme = findById(id);
        theme.validateDelete(loginUser.getId());

        repository.delete(theme);
    }

    private Theme findById(Long id){
        return repository.findById(id)
            .orElseThrow(NotFoundException::new);
    }

}
