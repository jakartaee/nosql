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
package ee.jakarta.tck.nosql.delete;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Fruit;
import ee.jakarta.tck.nosql.factories.FruitListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Deleting entities through converted attributes")
public class DeleteFieldConvertTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When deleting entities through converted attribute equality")
    class WhenTheDeletionUsesConvertedAttributeEqualityCondition {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should delete entities matching the selected converted value")
        void shouldDeleteEntitiesMatchingTheSelectedConvertedValue(List<Fruit> entities) {
            // Given
            entities.forEach(template::insert);
            var quantity = entities.getFirst().getQuantity();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Fruit.class)
                        .where("quantity")
                        .eq(quantity)
                        .execute();

                // Then
                assertThat(template.select(Fruit.class)
                        .where("quantity")
                        .eq(quantity)
                        .result())
                        .as("entities matching the deleted converted value")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through converted attribute comparison")
    class WhenTheDeletionUsesConvertedAttributeComparisonCondition {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should delete entities greater than the converted reference value")
        void shouldDeleteEntitiesGreaterThanTheConvertedReferenceValue(List<Fruit> entities) {
            // Given
            entities.forEach(template::insert);
            var quantity = entities.getFirst().getQuantity() - 1;

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Fruit.class)
                        .where("quantity")
                        .gt(quantity)
                        .execute();

                // Then
                assertThat(template.select(Fruit.class)
                        .where("quantity")
                        .gt(quantity)
                        .result())
                        .as("entities greater than the deleted converted value")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should delete entities less than the converted reference value")
        void shouldDeleteEntitiesLessThanTheConvertedReferenceValue(List<Fruit> entities) {
            // Given
            entities.forEach(template::insert);
            var quantity = entities.getFirst().getQuantity() + 10;

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Fruit.class)
                        .where("quantity")
                        .lt(quantity)
                        .execute();

                // Then
                assertThat(template.select(Fruit.class)
                        .where("quantity")
                        .lt(quantity)
                        .result())
                        .as("entities less than the deleted converted value")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through converted attribute ranges")
    class WhenTheDeletionUsesConvertedAttributeRangeCondition {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should delete entities within the selected converted range")
        void shouldDeleteEntitiesWithinTheSelectedConvertedRange(List<Fruit> entities) {
            // Given
            entities.forEach(template::insert);
            var quantity = entities.getFirst().getQuantity();
            var minimum = quantity - 5;
            var maximum = quantity + 5;

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Fruit.class)
                        .where("quantity")
                        .between(minimum, maximum)
                        .execute();

                // Then
                assertThat(template.select(Fruit.class)
                        .where("quantity")
                        .between(minimum, maximum)
                        .result())
                        .as("entities within the deleted converted range")
                        .isEmpty();
            });
        }
    }

    private void assertDeleteOrUnsupported(Runnable scenario) {
        try {
            scenario.run();
        } catch (UnsupportedOperationException exception) {
            assertThat(exception)
                    .as("delete operations may be unsupported by the provider")
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
