package com.avatar.trip.plan.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.avatar.trip.plan.authority.application.AuthorityService;
import com.avatar.trip.plan.common.domain.Role;
import com.avatar.trip.plan.authority.domain.Authority;
import com.avatar.trip.plan.authority.domain.AuthorityRepository;
import com.avatar.trip.plan.exception.TokenValidationException;
import com.avatar.trip.plan.user.domain.User;
import com.avatar.trip.plan.user.domain.UserAuthority;
import com.avatar.trip.plan.user.domain.UserRepository;
import com.avatar.trip.plan.user.dto.UpdateUserRequest;
import com.avatar.trip.plan.user.dto.UserRequest;
import com.avatar.trip.plan.user.dto.UserResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String TEST_EMAIL = "test@email.com";
    private static final String TEST_PASSWORD = "11111";
    private static final String TEST_NICKNAME = "테스트유저";

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityService authorityService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    Authority userRole = Authority.from(Role.USER);

    @Test
    void createUser() {

        when(passwordEncoder.encode(any()))
            .thenReturn(TEST_PASSWORD);
        when(authorityService.findByRole(any()))
            .thenReturn(userRole);
        when(userRepository.save(any(User.class)))
            .thenReturn(User.of(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME, List.of(UserAuthority.of(userRole))));

        //given email, password, nickname
        UserRequest userRequest = new UserRequest(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);

        //when
        UserResponse response = userService.create(userRequest);
        //then
        assertAll(
            () -> assertThat(response.getEmail()).isEqualTo(TEST_EMAIL),
            () -> assertThat(response.getNickname()).isEqualTo(TEST_NICKNAME)
        );
    }

    @Test
    void updateUser() {
        String updatePassword = "1234";
        String updateNickname = "update";

        when(passwordEncoder.encode(any()))
            .thenReturn(updatePassword);
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(User.of(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME, List.of(UserAuthority.of(userRole)))));
        when(userRepository.save(any(User.class)))
            .thenReturn(User.of(TEST_EMAIL, updatePassword, updateNickname, List.of(UserAuthority.of(userRole))));

        //when
        UserResponse response = userService.updateInfo(1L, new UpdateUserRequest(updatePassword, updateNickname));

        //then
        assertThat(response.getNickname()).isEqualTo(updateNickname);
    }

    @Test
    void existEmail() {

        when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(User.of(TEST_EMAIL, TEST_PASSWORD)));

        boolean response = userService.existEmail(TEST_EMAIL);

        assertTrue(response);
    }

    @Test
    void updateActivate() {
        User user = User.of(TEST_EMAIL, TEST_PASSWORD);
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));

        when(userRepository.save(any()))
            .thenReturn(user);

        UserResponse response = userService.updateActivate(1L, false);

        assertFalse(response.isActivate());
    }

    @Test
    void updateRefreshToken() {
        User user = User.of(TEST_EMAIL, TEST_PASSWORD);

        when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
            .thenReturn(user);

        userService.updateRefreshToken(TEST_EMAIL, "refreshToken");

        assertThat(user.getRefreshToken()).isNotEmpty();
    }

    @Test
    void checkRefreshToken_logout() {
        User user = User.of(TEST_EMAIL, TEST_PASSWORD);
        when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.checkRefreshTokenByEmail(TEST_EMAIL, "invalid"))
            .isInstanceOf(TokenValidationException.class)
            .hasMessage("로그아웃한 사용자 입니다.");

    }

    @Test
    void checkRefreshToken_notValid() {
        User user = User.of(TEST_EMAIL, TEST_PASSWORD);
        user.updateRefreshToken("refreshToken");
        when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.checkRefreshTokenByEmail(TEST_EMAIL, "invalid"))
            .isInstanceOf(TokenValidationException.class)
            .hasMessage("토큰이 일치하지 않습니다.");
    }
}