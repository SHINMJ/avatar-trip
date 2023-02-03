package com.avatar.trip.plan.theme.ui;

import com.avatar.trip.plan.theme.application.ThemeService;
import com.avatar.trip.plan.theme.dto.ThemeRequest;
import com.avatar.trip.plan.theme.dto.ThemeResponse;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService service;

    @PostMapping()
    public ResponseEntity create(@RequestBody ThemeRequest request,
        @AuthenticationPrincipal LoginUser loginUser){
        ThemeResponse response = service.create(request, loginUser);
        return ResponseEntity.created(URI.create("/themes/"+response.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> findById(@PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginUser){
        ThemeResponse response = service.findResponseById(id, loginUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Page<ThemeResponse>> findAll(Pageable pageable,
        @AuthenticationPrincipal LoginUser loginUser){
        Page<ThemeResponse> response = service.findAll(loginUser, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by/users")
    public ResponseEntity<Slice<ThemeResponse>> findAllByUser(Pageable pageable,
        @AuthenticationPrincipal LoginUser loginUser){
        Slice<ThemeResponse> response = service.findAllByUser(loginUser, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginUser){
        service.delete(id, loginUser);
        return ResponseEntity.noContent().build();
    }
}
