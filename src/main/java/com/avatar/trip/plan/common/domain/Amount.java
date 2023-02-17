package com.avatar.trip.plan.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Amount {

    private BigDecimal amount;

    private Amount(BigDecimal amount) {
        this.amount = amount;
    }

    public static Amount valueOf(BigDecimal amount){
        return new Amount(amount);
    }

    public static Amount valueOf(long amount) {
        return new Amount(BigDecimal.valueOf(amount));
    }

    public static Amount valueOf(double amount) {
        return new Amount(BigDecimal.valueOf(amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Amount amount1 = (Amount) o;
        return amount.equals(amount1.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
