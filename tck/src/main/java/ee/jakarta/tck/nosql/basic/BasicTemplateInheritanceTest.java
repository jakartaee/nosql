/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package ee.jakarta.tck.nosql.basic;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Drink;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.DrinkSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The basic template operations for inheritance hierarchy entities")
public class BasicTemplateInheritanceTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateInheritanceTest.class.getName());

    @Nested
    @DisplayName("When inserting an inheritance hierarchy entity")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(DrinkSupplier.class)
        @DisplayName("Should persist the inheritance hierarchy entity: {0}")
        void shouldInsert(Drink entity) {

            // Given

            // When
            var drink = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(drink)
                        .as("inserted drink")
                        .isNotNull();
                softly.assertThat(drink.getId())
                        .as("inserted drink id")
                        .isNotNull();
                softly.assertThat(drink.getName())
                        .as("inserted drink name")
                        .isEqualTo(entity.getName());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(DrinkSupplier.class)
        @DisplayName("Should persist the inheritance hierarchy entity with TTL when supported: {0}")
        void shouldInsertWithTtl(Drink drink) {
            try {
                // Given

                // When
                var insertedDrink = template.insert(drink, Duration.ofMinutes(10));

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(insertedDrink)
                            .as("inserted drink with ttl")
                            .isNotNull();
                    softly.assertThat(insertedDrink.getId())
                            .as("inserted drink with ttl id")
                            .isNotNull();
                    softly.assertThat(insertedDrink.getName())
                            .as("inserted drink with ttl name")
                            .isEqualTo(drink.getName());
                });
            } catch (UnsupportedOperationException exception) {
                LOGGER.info("TTL operation not supported by this database: " + exception.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("When updating an inheritance hierarchy entity")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(DrinkSupplier.class)
        @DisplayName("Should update the inheritance hierarchy entity: {0}")
        void shouldUpdate(Drink entity) {

            // Given
            var insertedDrink = template.insert(entity);
            insertedDrink.setName(insertedDrink.getName() + " Updated");

            // When
            var updatedDrink = template.update(insertedDrink);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedDrink)
                        .as("updated drink")
                        .isNotNull();
                softly.assertThat(updatedDrink.getId())
                        .as("updated drink id")
                        .isEqualTo(insertedDrink.getId());
                softly.assertThat(updatedDrink.getName())
                        .as("updated drink name")
                        .isEqualTo(insertedDrink.getName());
            });
        }
    }

    @Nested
    @DisplayName("When removing an inheritance hierarchy entity")
    class WhenTheRemoval {

        @ParameterizedTest
        @ArgumentsSource(DrinkSupplier.class)
        @DisplayName("Should remove the inheritance hierarchy entity: {0}")
        void shouldDelete(Drink entity) {

            // Given
            var insertedDrink = template.insert(entity);

            // When
            template.delete(insertedDrink.getClass(), insertedDrink.getId());
            var deletedDrink = template.find(insertedDrink.getClass(), insertedDrink.getId());

            // Then
            assertThat(deletedDrink)
                    .as("drink after deletion")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("When searching for an inheritance hierarchy entity")
    class WhenTheSearch {

        @ParameterizedTest
        @ArgumentsSource(DrinkSupplier.class)
        @DisplayName("Should return the inheritance hierarchy entity: {0}")
        void shouldFind(Drink entity) {

            // Given
            var insertedDrink = template.insert(entity);

            // When
            var foundDrink = template.find(insertedDrink.getClass(), insertedDrink.getId());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundDrink)
                        .as("found drink optional")
                        .isPresent();
                foundDrink.ifPresent(drink -> {
                    softly.assertThat(drink.getId())
                            .as("found drink id")
                            .isEqualTo(insertedDrink.getId());
                    softly.assertThat(drink.getName())
                            .as("found drink name")
                            .isEqualTo(insertedDrink.getName());
                });
            });
        }
    }

    @Nested
    @DisplayName("When validating inheritance hierarchy entities")
    class WhenTheValidation {

        @Test
        @DisplayName("Should reject a null entity during insertion")
        void shouldRejectNullEntityOnInsert() {

            // Given

            // When / Then
            assertThatThrownBy(() -> template.insert(null))
                    .as("null entity insertion")
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should reject a null entity during update")
        void shouldRejectNullEntityOnUpdate() {

            // Given

            // When / Then
            assertThatThrownBy(() -> template.update((Person) null))
                    .as("null entity update")
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
