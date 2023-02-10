package com.avatar.trip.plan.party.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.avatar.trip.plan.exception.BusinessException;
import com.avatar.trip.plan.exception.PhoneNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 휴대폰 번호
 * 첫번째 자리 : 010(011,016,017,018,019) 허용
 * 두번째 자리 : 3~4 자리 숫자
 * 세번째 자리 : 4 자리 숫자
 * 자릿수 사이 : 아무것도 표기 하지 않거나, '-', '.' 허용
 */
class PhoneNumberTest {

    @ParameterizedTest
    @ValueSource(strings = {"01011111111", "011-1111-1111", "016-111-2222", "017.111.1111", "018-988-2938", "019.2020.2222"})
    void created(String input) {
        PhoneNumber phoneNumber = PhoneNumber.valueOf(input);

        assertThat(phoneNumber.toString()).isEqualTo(input.replaceAll("[^0-9]", ""));
    }

    @DisplayName("한글,영어, '-','.' 제외한 특수문자 입력 시 실패")
    @ParameterizedTest
    @ValueSource(strings = {"010 1234 1234","010/1111/1111", "phonenumber", "010phonenumber", "11111111111", "010#@$#phone", "010폰넘버"})
    void created_failed_becauseNotAllowedCharacters(String input){
        created_failed(input);
    }

    @DisplayName("첫번째 자리 정합성 실패")
    @ParameterizedTest
    @ValueSource(strings = {"012-1234-1234","013-1234-1234","01411111111","0152321234"})
    void created_failed_becauseFirstPosition(String input) {
        created_failed(input);
    }

    @DisplayName("두번째, 세번째 자리 정합성 실패")
    @ParameterizedTest
    @ValueSource(strings = {"010-22-1234", "011-23445-2343", "010.2.9292", "010-1111111111", "010023200000", "010.2345.222222", "010.2345.23"})
    void created_failed_becauseDigits(String input) {
        created_failed(input);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void created_failed_becauseNullAndEmpty(String input) {
        created_failed(input);
    }

    private void created_failed(String input){
        assertThatThrownBy(() -> PhoneNumber.valueOf(input))
            .isInstanceOf(PhoneNumberException.class);
    }
}