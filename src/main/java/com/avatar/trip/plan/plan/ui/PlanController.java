package com.avatar.trip.plan.plan.ui;

import com.avatar.trip.plan.plan.application.PlanService;
import com.avatar.trip.plan.plan.dto.PlanRequest;
import com.avatar.trip.plan.plan.dto.PlanResponse;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService service;

    @PostMapping()
    public ResponseEntity create(@RequestBody PlanRequest request,
        @AuthenticationPrincipal LoginUser loginUser){
        PlanResponse response = service.create(loginUser, request);
        return ResponseEntity.created(URI.create("/plans/"+response.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> findById(@PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginUser){
        PlanResponse response = service.findResponseById(loginUser, id);
        return ResponseEntity.ok(response);
    }
}
