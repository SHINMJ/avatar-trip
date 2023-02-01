package com.avatar.trip.plan.authority.ui;

import com.avatar.trip.plan.authority.application.AuthorityService;
import com.avatar.trip.plan.authority.dto.AuthorityRequest;
import com.avatar.trip.plan.authority.dto.AuthorityResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authorities")
public class AuthorityController {

    private final AuthorityService service;

    @GetMapping()
    public ResponseEntity<Slice<AuthorityResponse>> findAll(@RequestParam int page, @RequestParam int size){
        Slice<AuthorityResponse> responses = service.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(responses);
    }

    @PostMapping()
    public ResponseEntity save(@RequestBody AuthorityRequest request){
        AuthorityResponse response = service.createAuthority(request);
        return ResponseEntity.created(URI.create("/authorities"+response.getId())).build();
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<AuthorityResponse> findByRole(@PathVariable String roleId){
        AuthorityResponse response = service.findByRoleId(roleId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
