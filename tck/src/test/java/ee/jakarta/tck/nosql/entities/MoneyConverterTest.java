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

import jakarta.nosql.AttributeConverter;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

class MoneyConverterTest {

    private final AttributeConverter<Money, String> converter = new MoneyConverter();

    @Test
    public void shouldConvertToDatabaseColumn() {
        Money money = new Money(Currency.getInstance("USD"), BigDecimal.valueOf(10));
        String convert = converter.convertToDatabaseColumn(money);
        Assertions.assertThat(convert).isEqualTo("USD 10");
    }

    @Test
    public void shouldConvertToEntityAttribute() {
        Money money = converter.convertToEntityAttribute("USD 10");
        SoftAssertions.assertSoftly(a -> {
            a.assertThat(money.currency().getCurrencyCode()).isEqualTo("USD");
            a.assertThat(money.value()).isEqualByComparingTo(BigDecimal.valueOf(10));
        });
    }
}