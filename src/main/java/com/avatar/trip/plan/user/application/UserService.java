package com.avatar.trip.plan.user.application;

import com.avatar.trip.plan.authority.application.AuthorityService;
import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.exception.NotFoundException;
import com.avatar.trip.plan.user.domain.User;
import com.avatar.trip.plan.user.domain.UserAuthority;
import com.avatar.trip.plan.user.domain.UserRepository;
import com.avatar.trip.plan.user.dto.UpdateUserRequest;
import com.avatar.trip.plan.user.dto.UserRequest;
import com.avatar.trip.plan.user.dto.UserResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(UserRequest userRequest){
        Authority userRole = authorityService.findByRole(Role.USER);
        User user = userRequest.toUser(passwordEncoder.encode(userRequest.getPassword()), List.of(UserAuthority.of(userRole)));
        User saved = repository.save(user);

        return UserResponse.of(saved);
    }

    public UserResponse updateInfo(Long id, UpdateUserRequest request){
        User user = findById(id);
        String encode = passwordEncoder.encode(request.getPassword());
        user.updateInfo(encode, request.getNickname());
        User saved = repository.save(user);
        return UserResponse.of(saved);
    }

    private User findById(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    public boolean existEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public UserResponse updateActivate(Long id, boolean activate) {
        User user = findById(id);
        user.updateActivate(activate);

        User saved = repository.save(user);
        return UserResponse.of(saved);
    }
}
