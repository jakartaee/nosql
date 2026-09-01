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
import ee.jakarta.tck.nosql.entities.Animal;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.AnimalSupplier;
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

@DisplayName("The basic template operations for mapped superclass entities")
public class BasicTemplateMappedSuperclassTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateMappedSuperclassTest.class.getName());

    @Nested
    @DisplayName("When inserting a mapped superclass entity")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(AnimalSupplier.class)
        @DisplayName("Should persist the mapped superclass entity: {0}")
        void shouldInsert(Animal entity) {

            // Given

            // When
            var animal = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(animal)
                        .as("inserted animal")
                        .isNotNull();
                softly.assertThat(animal.getId())
                        .as("inserted animal id")
                        .isNotNull();
                softly.assertThat(animal.getName())
                        .as("inserted animal name")
                        .isEqualTo(entity.getName());
                softly.assertThat(animal.getScientificName())
                        .as("inserted animal scientific name")
                        .isEqualTo(entity.getScientificName());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(AnimalSupplier.class)
        @DisplayName("Should persist the mapped superclass entity with TTL when supported: {0}")
        void shouldInsertWithTtl(Animal animal) {
            try {
                // Given

                // When
                var insertedAnimal = template.insert(animal, Duration.ofMinutes(10));

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(insertedAnimal)
                            .as("inserted animal with ttl")
                            .isNotNull();
                    softly.assertThat(insertedAnimal.getId())
                            .as("inserted animal with ttl id")
                            .isNotNull();
                    softly.assertThat(insertedAnimal.getName())
                            .as("inserted animal with ttl name")
                            .isEqualTo(animal.getName());
                });
            } catch (UnsupportedOperationException exception) {
                LOGGER.info("TTL operation not supported by this database: " + exception.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("When updating a mapped superclass entity")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(AnimalSupplier.class)
        @DisplayName("Should update the mapped superclass entity: {0}")
        void shouldUpdate(Animal entity) {

            // Given
            var insertedAnimal = template.insert(entity);
            insertedAnimal.setSpecies("Updated Species");

            // When
            var updatedAnimal = template.update(insertedAnimal);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedAnimal)
                        .as("updated animal")
                        .isNotNull();
                softly.assertThat(updatedAnimal.getId())
                        .as("updated animal id")
                        .isEqualTo(insertedAnimal.getId());
                softly.assertThat(updatedAnimal.getSpecies())
                        .as("updated animal species")
                        .isEqualTo("Updated Species");
            });
        }
    }

    @Nested
    @DisplayName("When removing a mapped superclass entity")
    class WhenTheRemoval {

        @ParameterizedTest
        @ArgumentsSource(AnimalSupplier.class)
        @DisplayName("Should remove the mapped superclass entity: {0}")
        void shouldDelete(Animal entity) {

            // Given
            var insertedAnimal = template.insert(entity);

            // When
            template.delete(Animal.class, insertedAnimal.getId());
            var deletedAnimal = template.find(Animal.class, insertedAnimal.getId());

            // Then
            assertThat(deletedAnimal)
                    .as("animal after deletion")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("When searching for a mapped superclass entity")
    class WhenTheSearch {

        @ParameterizedTest
        @ArgumentsSource(AnimalSupplier.class)
        @DisplayName("Should return the mapped superclass entity: {0}")
        void shouldFind(Animal entity) {

            // Given
            var insertedAnimal = template.insert(entity);

            // When
            var foundAnimal = template.find(Animal.class, insertedAnimal.getId());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundAnimal)
                        .as("found animal optional")
                        .isPresent();
                foundAnimal.ifPresent(animal -> {
                    softly.assertThat(animal.getId())
                            .as("found animal id")
                            .isEqualTo(insertedAnimal.getId());
                    softly.assertThat(animal.getName())
                            .as("found animal name")
                            .isEqualTo(insertedAnimal.getName());
                });
            });
        }
    }

    @Nested
    @DisplayName("When validating mapped superclass entities")
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
