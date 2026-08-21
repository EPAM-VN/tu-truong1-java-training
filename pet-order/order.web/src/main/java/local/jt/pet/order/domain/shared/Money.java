package local.jt.pet.order.domain.shared;

import java.math.BigDecimal;

public final record Money(BigDecimal amount, Currency currency) {

    public static Money add(Money first, Money second) {
        if (!first.currency().equals(second.currency())) {
            throw new IllegalArgumentException("Currencies have to be equal");
        }

        return new Money(first.amount().add(second.amount()), first.currency());
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO, Currency.NONE);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public boolean isZero() {
        return this.equals(zero(this.currency));
    }
}
