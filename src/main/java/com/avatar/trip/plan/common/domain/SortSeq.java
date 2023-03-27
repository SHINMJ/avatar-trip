package com.avatar.trip.plan.common.domain;

import static java.lang.Integer.parseInt;

import com.avatar.trip.plan.exception.BusinessException;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SortSeq {
    private static final Integer MIN_VALUE = 1;

    private Integer sort;

    private SortSeq(Integer sort) {
        validate(sort);
        this.sort = sort;
    }

    private void validate(Integer sort) {
        if (sort < MIN_VALUE){
            throw new BusinessException(String.format("순서는 %d 보다 커야 합니다.", MIN_VALUE));
        }
    }

    public static SortSeq valueOf(Integer sort){
        return new SortSeq(sort);
    }

    public static SortSeq valueOf(String sort){
        return new SortSeq(parseInt(sort));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortSeq sortSeq = (SortSeq) o;
        return sort.equals(sortSeq.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sort);
    }
}
