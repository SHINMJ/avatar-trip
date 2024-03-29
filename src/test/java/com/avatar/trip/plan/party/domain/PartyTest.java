package com.avatar.trip.plan.party.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.exception.RequiredArgumentException;
import com.avatar.trip.plan.plan.domain.Period;
import com.avatar.trip.plan.plan.domain.Plan;
import com.avatar.trip.plan.plan.domain.PlanTheme;
import com.avatar.trip.plan.theme.domain.Theme;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class PartyTest {
    private static final List<PlanTheme> THEMES = List.of(PlanTheme.of(Theme.of("테마", 1L)));
    private static final Plan PLAN = Plan.of(1L, 1L, THEMES, Period.of(1,2));


    @Test
    void created() {
        Party party = Party.of(PhoneNumber.valueOf("010-1111-1111"), Permission.EDIT, PLAN);

        assertThat(party.getSend()).isFalse();
    }

    @DisplayName("일행 생성 실패 - null")
    @ParameterizedTest
    @NullSource
    void created_failed(Object input) {
        assertThatThrownBy(() -> Party.of((PhoneNumber) input, Permission.EDIT, PLAN))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Party.of(PhoneNumber.valueOf("01011111111"), (Permission) input,
            PLAN))
            .isInstanceOf(RequiredArgumentException.class);

        assertThatThrownBy(() -> Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT,
            (Plan) input))
            .isInstanceOf(RequiredArgumentException.class);
    }

    @DisplayName("사용자 id 수정")
    @Test
    void setUserId() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, PLAN);

        assertThat(party.getUserId()).isNull();

        party.setUserId(1L);

        assertThat(party.getUserId()).isNotNull();
    }

    @DisplayName("문자 보내기 확인")
    @Test
    void sendSMS() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, PLAN);

        assertThat(party.getSend()).isFalse();

        party.sendSMS();

        assertThat(party.getSend()).isTrue();

    }

    @DisplayName("권한 수정")
    @Test
    void updatePermission() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, PLAN);

        assertThat(party.getPermission()).isEqualTo(Permission.EDIT);

        party.updatePermission(Permission.READ);

        assertThat(party.getPermission()).isEqualTo(Permission.READ);
    }

    @DisplayName("사용자 아이디가 달라서 편집권한 없음")
    @Test
    void canEdit_falseBecauseUserId() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, PLAN);

        assertFalse(party.edit(1L));
    }

    @DisplayName("사용자 아이디가 같지만 권한이 READ임")
    @Test
    void canEdit_falseBecausePermissionREAD() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.READ, PLAN);
        party.setUserId(1L);
        assertFalse(party.edit(1L));
    }

    @DisplayName("편집권한 있음")
    @Test
    void canEdit_success() {
        Party party = Party.of(PhoneNumber.valueOf("01011111111"), Permission.EDIT, PLAN);
        party.setUserId(1L);
        assertTrue(party.edit(1L));
    }
}