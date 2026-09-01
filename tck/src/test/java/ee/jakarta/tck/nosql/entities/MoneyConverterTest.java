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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MoneyConverterTest {

    private final AttributeConverter<Money, String> converter = new MoneyConverter();

    @Nested
    @DisplayName("When converting money to a database value")
    class WhenTheDatabaseValueIsCreated {

        @Test
        @DisplayName("Should return the currency and amount")
        void shouldReturnTheCurrencyAndAmount() {

            // Given
            var money = new Money(Currency.getInstance("USD"), BigDecimal.valueOf(10));

            // When
            var databaseValue = converter.convertToDatabaseColumn(money);

            // Then
            assertThat(databaseValue)
                    .as("serialized money")
                    .isEqualTo("USD 10");
        }

        @Test
        @DisplayName("Should return null when money is null")
        void shouldReturnNullWhenMoneyIsNull() {

            // When
            var databaseValue = converter.convertToDatabaseColumn(null);

            // Then
            assertThat(databaseValue)
                    .as("serialized null money")
                    .isNull();
        }
    }

    @Nested
    @DisplayName("When converting a database value to money")
    class WhenTheMoneyIsCreated {

        @Test
        @DisplayName("Should return the currency and amount")
        void shouldReturnTheCurrencyAndAmount() {

            // When
            var money = converter.convertToEntityAttribute("USD 10");

            // Then
            assertSoftly(softly -> {
                softly.assertThat(money.currency().getCurrencyCode())
                        .as("currency code")
                        .isEqualTo("USD");
                softly.assertThat(money.value())
                        .as("money amount")
                        .isEqualByComparingTo(BigDecimal.valueOf(10));
            });
        }

        @Test
        @DisplayName("Should return null when the database value is null")
        void shouldReturnNullWhenTheDatabaseValueIsNull() {

            // When
            var money = converter.convertToEntityAttribute(null);

            // Then
            assertThat(money)
                    .as("money converted from a null database value")
                    .isNull();
        }
    }
}