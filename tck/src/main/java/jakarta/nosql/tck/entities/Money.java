package jakarta.nosql.tck.entities;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Money(Currency currency, BigDecimal value) {
    public Money {
        Objects.requireNonNull(currency, "currency is required");
        Objects.requireNonNull(value, "value is required");
    }

    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " + value.toPlainString();
    }
}
