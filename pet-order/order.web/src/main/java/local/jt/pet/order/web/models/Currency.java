package local.jt.pet.order.web.models;

import java.util.List;

public record Currency(String code) {
    static final Currency NONE = new Currency("");
    public static final Currency USD = new Currency("USD");
    public static final Currency EUR = new Currency("EUR");

    public static Currency of(String code) {
        return ALL()
                .stream()
                .filter(c -> c.code.equals(code))
                .findFirst()
                .orElse(NONE);
    }
    public static final List<Currency> ALL() {
        return List.of(USD, EUR);
    }
}
