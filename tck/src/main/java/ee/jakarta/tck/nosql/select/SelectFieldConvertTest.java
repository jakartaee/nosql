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
package ee.jakarta.tck.nosql.select;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Fruit;
import ee.jakarta.tck.nosql.factories.FruitListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The query execution exploring a converted attribute")
public class SelectFieldConvertTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting entities by converted attribute equality")
    class WhenTheConvertedAttributeEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return only entities with the requested converted attribute value")
        void shouldReturnOnlyMatchingEntities(List<Fruit> entities) {

            // Given
            insertFruits(entities);
            Long targetQuantity = entities.getFirst().getQuantity();

            try {
                // When
                List<Fruit> result = template.select(Fruit.class)
                        .where("quantity")
                        .eq(targetQuantity)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("fruits returned for the converted quantity equality filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("fruits matching the requested quantity")
                            .allMatch(fruit -> fruit.getQuantity().equals(targetQuantity));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by converted attribute greater than a threshold")
    class WhenTheConvertedAttributeGreaterThanSelection {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return only entities whose converted attribute is greater than the requested value")
        void shouldReturnOnlyMatchingEntities(List<Fruit> entities) {

            // Given
            insertFruits(entities);
            long targetQuantity = entities.getFirst().getQuantity() - 1;

            try {
                // When
                List<Fruit> result = template.select(Fruit.class)
                        .where("quantity")
                        .gt(targetQuantity)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("fruits returned for the converted quantity greater-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("fruits with quantity greater than the requested value")
                            .allMatch(fruit -> fruit.getQuantity() > targetQuantity);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by converted attribute less than a threshold")
    class WhenTheConvertedAttributeLessThanSelection {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return only entities whose converted attribute is less than the requested value")
        void shouldReturnOnlyMatchingEntities(List<Fruit> entities) {

            // Given
            insertFruits(entities);
            long targetQuantity = entities.getFirst().getQuantity() + 10;

            try {
                // When
                List<Fruit> result = template.select(Fruit.class)
                        .where("quantity")
                        .lt(targetQuantity)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("fruits returned for the converted quantity less-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("fruits with quantity less than the requested value")
                            .allMatch(fruit -> fruit.getQuantity() < targetQuantity);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by a converted attribute range")
    class WhenTheConvertedAttributeRangeSelection {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return only entities whose converted attribute falls within the requested range")
        void shouldReturnOnlyMatchingEntities(List<Fruit> entities) {

            // Given
            insertFruits(entities);
            long targetQuantity = entities.getFirst().getQuantity();
            long lowerBound = targetQuantity - 5;
            long upperBound = targetQuantity + 5;

            try {
                // When
                List<Fruit> result = template.select(Fruit.class)
                        .where("quantity")
                        .between(lowerBound, upperBound)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("fruits returned for the converted quantity range filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("fruits whose quantity falls within the requested range")
                            .allMatch(fruit -> {
                                long quantity = fruit.getQuantity();
                                return quantity >= lowerBound && quantity <= upperBound;
                            });
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    private void insertFruits(List<Fruit> entities) {
        entities.forEach(template::insert);
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported converted field select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
