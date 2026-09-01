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
package ee.jakarta.tck.nosql.basic;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Fruit;
import ee.jakarta.tck.nosql.entities.Money;
import ee.jakarta.tck.nosql.factories.FruitSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The basic template operations for entities with converted attributes")
public class BasicConverterFieldTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When inserting an entity with a converted attribute")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(FruitSupplier.class)
        @DisplayName("Should persist the entity with the converted attribute: {0}")
        void shouldInsert(Fruit entity) {

            // Given

            // When
            var fruit = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(fruit)
                        .as("inserted fruit")
                        .isNotNull();
                softly.assertThat(fruit.getId())
                        .as("inserted fruit id")
                        .isNotNull();
                softly.assertThat(fruit.getName())
                        .as("inserted fruit name")
                        .isEqualTo(entity.getName());
                softly.assertThat(fruit.getPrice())
                        .as("inserted fruit price")
                        .isEqualTo(entity.getPrice());
            });
        }
    }

    @Nested
    @DisplayName("When updating an entity with a converted attribute")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(FruitSupplier.class)
        @DisplayName("Should update the converted price: {0}")
        void shouldUpdate(Fruit entity) {

            // Given
            var insertedFruit = template.insert(entity);
            insertedFruit.setPrice(new Money(
                    insertedFruit.getPrice().currency(),
                    insertedFruit.getPrice().value().add(BigDecimal.TEN)
            ));

            // When
            var updatedFruit = template.update(insertedFruit);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedFruit)
                        .as("updated fruit")
                        .isNotNull();
                softly.assertThat(updatedFruit.getPrice().value())
                        .as("updated fruit price value")
                        .isEqualTo(insertedFruit.getPrice().value());
            });
        }
    }

    @Nested
    @DisplayName("When removing an entity with a converted attribute")
    class WhenTheRemoval {

        @ParameterizedTest
        @ArgumentsSource(FruitSupplier.class)
        @DisplayName("Should remove the entity with the converted attribute: {0}")
        void shouldDelete(Fruit entity) {

            // Given
            var insertedFruit = template.insert(entity);

            // When
            template.delete(Fruit.class, insertedFruit.getId());
            var deletedFruit = template.find(Fruit.class, insertedFruit.getId());

            // Then
            assertThat(deletedFruit)
                    .as("fruit after deletion")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("When searching for an entity with a converted attribute")
    class WhenTheSearch {

        @ParameterizedTest
        @ArgumentsSource(FruitSupplier.class)
        @DisplayName("Should return the entity with the converted attribute: {0}")
        void shouldFind(Fruit entity) {

            // Given
            var insertedFruit = template.insert(entity);

            // When
            var foundFruit = template.find(Fruit.class, insertedFruit.getId());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundFruit)
                        .as("found fruit optional")
                        .isPresent();
                foundFruit.ifPresent(fruit -> softly.assertThat(fruit.getPrice())
                        .as("found fruit price")
                        .isEqualTo(insertedFruit.getPrice()));
            });
        }
    }
}
