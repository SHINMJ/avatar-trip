package com.avatar.trip.plan.schedule.ui;

import com.avatar.trip.plan.schedule.application.ScheduleService;
import com.avatar.trip.plan.schedule.dto.ScheduleBudgetRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleNoteRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService service;

    @PostMapping()
    public ResponseEntity create(@RequestBody ScheduleRequest request, @AuthenticationPrincipal
        LoginUser loginUser){
        ScheduleResponse response = service.create(loginUser, request);
        return ResponseEntity.created(URI.create("/schedules/"+response.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser){
        ScheduleResponse response = service.findResponseById(loginUser, id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/budgets/{id}")
    public ResponseEntity inputBudget(@PathVariable Long id, @RequestBody ScheduleBudgetRequest request, @AuthenticationPrincipal LoginUser loginUser){
        ScheduleResponse response = service.inputBudget(loginUser, id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity takeNotes(@PathVariable Long id, @RequestBody ScheduleNoteRequest request, @AuthenticationPrincipal LoginUser loginUser){
        ScheduleResponse response = service.takeNotes(loginUser, id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planId}/budgets/{day}")
    public ResponseEntity<BigDecimal> findTotalBudgetByDay(@PathVariable Long planId, @PathVariable int day, @AuthenticationPrincipal LoginUser loginUser){
        BigDecimal budget = service.findTotalBudgetByDay(loginUser, planId, day);
        return ResponseEntity.ok().body(budget);
    }

    @GetMapping("/{planId}/{day}")
    public ResponseEntity<List<ScheduleResponse>> findAllByPlanAndDay(@PathVariable Long planId, @PathVariable int day, @AuthenticationPrincipal LoginUser loginUser){
        List<ScheduleResponse> responses = service.findAllByPlanAndDay(loginUser, planId, day);
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser){
        service.delete(loginUser, id);
        return ResponseEntity.noContent().build();
    }
}
