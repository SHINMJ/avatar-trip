package com.avatar.trip.plan.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/themeTestSetup.sql")
class ThemeRepositoryTest {

    @Autowired
    ThemeRepository themeRepository;

    @Test
    void createTheme() {
        Theme saved = themeRepository.save(Theme.of("아이와", 1L));

        themeRepository.flush();

        assertThat(saved.getName()).isEqualTo(ThemeName.valueOf("아이와"));
    }

    @Test
    void findAll() {
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("등록된 모든 테마 중 userId에 해당하는 테마와 관리자 테마 조회")
    void findAllByUserId() {
        themeRepository.save(Theme.of("아이와", 2L));

        Slice<Theme> themes = themeRepository.findAllByOwnerIdOrIsAdminTrue(2L,  PageRequest.of(0,10 ));

        assertThat(themes.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("페이지 형태로 모든 테마 조회")
    void findPageAll() {
        themeRepository.save(Theme.of("아이와", 2L));

        Page<Theme> pageAll = themeRepository.findAllBy(PageRequest.of(0, 10));

        assertThat(pageAll.getTotalElements()).isEqualTo(3);
    }
}