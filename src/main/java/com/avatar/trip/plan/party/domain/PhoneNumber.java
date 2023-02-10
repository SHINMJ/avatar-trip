package com.avatar.trip.plan.party.domain;

import com.avatar.trip.plan.exception.PhoneNumberException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 휴대폰 번호
 * 첫번째 자리 : 010(011,016,017,018,019) 허용
 * 두번째 자리 : 3~4 자리 숫자
 * 세번째 자리 : 4 자리 숫자
 * 자릿수 사이 : 아무것도 표기 하지 않거나, '-', '.' 허용
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PhoneNumber {
    private static final String EXCEPTION_MESSAGE = "휴대폰 번호를 제대로 입력해 주세요.";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$");
    private static final String REPLACE_REGEX = "[^0-9]";

    private String phoneNumber;

    private PhoneNumber(String phoneNumber) {
        validate(phoneNumber);
        this.phoneNumber = parse(phoneNumber);
    }

    public static PhoneNumber valueOf(String phoneNumber){
        return new PhoneNumber(phoneNumber);
    }

    private void validate(String phoneNumber) {
        if(!StringUtils.hasText(phoneNumber)){
            throw new PhoneNumberException(EXCEPTION_MESSAGE);
        }
        if(!matcher(phoneNumber)){
            throw new PhoneNumberException(EXCEPTION_MESSAGE);
        }
    }

    private boolean matcher(String phoneNumber){
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }

    private String parse(String phoneNumber){
        return phoneNumber.replaceAll(REPLACE_REGEX, "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneNumber that = (PhoneNumber) o;
        return phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }

    @Override
    public String toString() {
        return phoneNumber;
    }
}
