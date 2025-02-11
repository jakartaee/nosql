/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.nosql.entities;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public record Money(Currency currency, BigDecimal value) {
    public Money {
        Objects.requireNonNull(currency, "currency is required");
        Objects.requireNonNull(value, "value is required");
    }

    public static Money of(Faker faker) {
        var currency = Currency.getInstance(faker.money().currencyCode());
        BigDecimal value = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 1000));
        return new Money(currency, value);
    }

    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " + value.toPlainString();
    }
}
