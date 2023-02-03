package com.avatar.trip.plan.theme.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.avatar.trip.plan.exception.CannotDeleteException;
import com.avatar.trip.plan.exception.NotFoundException;
import com.avatar.trip.plan.exception.UnauthorizedException;
import com.avatar.trip.plan.theme.domain.Theme;
import com.avatar.trip.plan.theme.domain.ThemeRepository;
import com.avatar.trip.plan.theme.dto.ThemeRequest;
import com.avatar.trip.plan.theme.dto.ThemeResponse;
import com.avatar.trip.plan.user.dto.LoginUser;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {
    static final Long ADMIN_ID = 1L;
    static final Long USER_ID = 2L;

    @Mock
    ThemeRepository repository;

    @InjectMocks
    ThemeService service;

    LoginUser loginUser = mock(LoginUser.class);

    @Test
    void createThemeOfUser() {
        ThemeRequest request = new ThemeRequest("아이와", false);
        LoginUser loginUser = mock(LoginUser.class);

        when(loginUser.getId())
            .thenReturn(1L);
        when(repository.save(any(Theme.class)))
            .thenReturn(request.toTheme(USER_ID));

        ThemeResponse response = service.create(request, loginUser);

        assertAll(
            () -> assertThat(response.getThemeName()).isEqualTo("아이와"),
            () -> assertFalse(response.isAdmin())
        );
    }

    @Test
    void createThemeOfAdmin() {
        ThemeRequest request = new ThemeRequest("아이와", true);


        when(loginUser.getId())
            .thenReturn(1L);
        when(repository.save(any(Theme.class)))
            .thenReturn(request.toTheme(ADMIN_ID));

        ThemeResponse response = service.create(request, loginUser);

        assertAll(
            () -> assertThat(response.getThemeName()).isEqualTo("아이와"),
            () -> assertTrue(response.isAdmin())
        );
    }

    @Test
    @DisplayName("등록된 모든 테마 중 userId에 해당하는 테마와 관리자 테마 조회")
    void findAllByUser() {
        Theme theme1 = Theme.ofAdmin("아이와", ADMIN_ID);
        Theme theme2 = Theme.of("아이와", USER_ID);
        PageRequest page = PageRequest.of(0, 2);

        when(loginUser.getId()).thenReturn(USER_ID);
        when(repository.findAllByOwnerIdOrIsAdminTrue(USER_ID, page))
            .thenReturn(new SliceImpl<>(List.of(theme1, theme2), page, false));

        Slice<ThemeResponse> responses = service.findAllByUser(loginUser, page);

        assertThat(responses.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 테마 조회")
    void findAllBy() {
        Theme theme1 = Theme.ofAdmin("아이와", ADMIN_ID);
        Theme theme2 = Theme.of("아이와", USER_ID);
        PageRequest page = PageRequest.of(0, 2);

        when(loginUser.isAdmin()).thenReturn(true);
        when(repository.findAllBy(page))
            .thenReturn(new PageImpl<>(List.of(theme1, theme2)));

        Page<ThemeResponse> responses = service.findAll(loginUser, page);

        assertThat(responses.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 테마 조회 실패 - 권한 없음")
    void findAllBy_failed() {
        PageRequest page = PageRequest.of(0, 2);

        when(loginUser.isAdmin()).thenReturn(false);

        assertThatThrownBy(() -> service.findAll(loginUser, page))
            .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("id로 한건 조회")
    void findById() {
        Theme theme = Theme.of("아이와", USER_ID);

        when(loginUser.getId())
            .thenReturn(USER_ID);
        when(repository.findById(1L))
            .thenReturn(Optional.of(theme));

        ThemeResponse response = service.findResponseById(1L, loginUser);

        assertAll(
            () -> assertThat(response.getOwnerId()).isEqualTo(theme.getOwnerId()),
            () -> assertThat(response.getThemeName()).isEqualTo(theme.getThemeName()),
            () -> assertFalse(response.isAdmin())
        );
    }

    @Test
    @DisplayName("id로 한건 조회 실패 - 다른 사용자의 테마는 조회할 수 없음. NotFoundException")
    void findById_failed() {
        Theme theme = Theme.of("아이와", ADMIN_ID);

        when(loginUser.getId())
            .thenReturn(USER_ID);
        when(repository.findById(1L))
            .thenReturn(Optional.of(theme));

        assertThatThrownBy(() -> service.findResponseById(1L, loginUser))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("다른 사용자의 테마 삭제 시 CannotDeleteException 발생")
    void deleteTheme_otherUserFail() {
        Theme theme = Theme.of("아이와", ADMIN_ID);

        when(loginUser.getId())
            .thenReturn(USER_ID);
        when(repository.findById(1L))
            .thenReturn(Optional.of(theme));

        assertThatThrownBy(() -> service.delete(1L, loginUser))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("이미 삭제된 테마 삭제 시 NotFoundException 발생")
    void deleteTheme_notFoundFail() {
        when(repository.findById(1L))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L, loginUser))
            .isInstanceOf(NotFoundException.class);
    }
}
