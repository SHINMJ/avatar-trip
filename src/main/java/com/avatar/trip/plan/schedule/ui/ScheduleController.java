package com.avatar.trip.plan.schedule.ui;

import com.avatar.trip.plan.schedule.application.ScheduleService;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
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
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService service;

    @PostMapping()
    public ResponseEntity create(@RequestBody ScheduleRequest request,
        @AuthenticationPrincipal LoginUser loginUser){
        ScheduleResponse response = service.created(loginUser, request);
        return ResponseEntity.created(URI.create("/schedules/"+response.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginUser){
        ScheduleResponse response = service.findResponseById(loginUser, id);
        return ResponseEntity.ok(response);
    }
}
