package com.avatar.trip.plan.authority.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.authority.domain.AuthorityRepository;
import com.avatar.trip.plan.authority.dto.AuthorityRequest;
import com.avatar.trip.plan.authority.dto.AuthorityResponse;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    @Mock
    AuthorityRepository authorityRepository;

    @InjectMocks
    AuthorityService authorityService;

    @DisplayName("사용자 권한 생성 성공")
    @Test
    void createFromUser_success() {
        Authority userRole = Authority.from(Role.USER);

        when(authorityRepository.save(any(Authority.class)))
            .thenReturn(userRole);

        AuthorityResponse res = authorityService.createAuthority(new AuthorityRequest("ROLE_USER", ""));

        assertAll(
            () -> assertThat(res.getRoleId()).isEqualTo(userRole.getRoleId()),
            () -> assertThat((res.getName())).isEqualTo(userRole.getName())
        );
    }

    @Test
    @DisplayName("권한목록 조회")
    void findAll_success() {
        Authority userRole = Authority.from(Role.USER);
        Authority adminRole = Authority.from(Role.ADMIN);

        when(authorityRepository.findAllBy(any(PageRequest.class)))
            .thenReturn(new SliceImpl<Authority>(List.of(adminRole, userRole)));

        Slice<AuthorityResponse> responses = authorityService.findAll(PageRequest.of(0,2));

        assertAll(
            () -> assertFalse(responses.hasNext()),
            () -> assertTrue(responses.hasContent())
        );
    }

    @Test
    @DisplayName("role 로 권한 조회")
    void findByRole_success() {
        Authority userRole = Authority.from(Role.USER);

        when(authorityRepository.findByRole(Role.USER))
            .thenReturn(Optional.of(userRole));

        AuthorityResponse response = authorityService.findByRole("ROLE_USER");

        assertThat(response.getRoleId()).isEqualTo(Role.USER.getKey());
    }

    @Test
    void deleteNotFound_failed() {
        when(authorityRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorityService.delete(1L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("해당 권한을 찾을 수 없습니다.");

    }
}