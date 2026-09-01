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
package ee.jakarta.tck.nosql.select;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Animal;
import ee.jakarta.tck.nosql.factories.AnimalListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The query execution using mapped superclass entities")
public class SelectMappedSuperclassTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting mapped superclass entities without filters")
    class WhenTheMappedSuperclassSelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return every inserted entity")
        void shouldReturnAllEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);

            try {
                // When
                List<Animal> result = template.select(Animal.class).result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned without filters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("number of animals returned without filters")
                            .hasSize(animals.size());
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting mapped superclass entities by name equality")
    class WhenTheNameEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return only entities with the requested name")
        void shouldReturnOnlyMatchingEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);
            String name = animals.getFirst().getName();

            try {
                // When
                List<Animal> result = template.select(Animal.class)
                        .where("name")
                        .eq(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned for the name equality filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("animals matching the requested name")
                            .allMatch(animal -> animal.getName().equals(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting mapped superclass entities by age greater than a threshold")
    class WhenTheAgeGreaterThanSelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return only entities older than the requested age")
        void shouldReturnOnlyMatchingEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);
            int secondElder = animals.stream()
                    .mapToInt(Animal::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            try {
                // When
                List<Animal> result = template.select(Animal.class)
                        .where("age")
                        .gt(secondElder)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned for the greater-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("animals older than the requested age")
                            .allMatch(animal -> animal.getAge() > secondElder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting mapped superclass entities by age less than a threshold")
    class WhenTheAgeLessThanSelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return only entities younger than the requested age")
        void shouldReturnOnlyMatchingEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);
            int secondElder = animals.stream()
                    .mapToInt(Animal::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            try {
                // When
                List<Animal> result = template.select(Animal.class)
                        .where("age")
                        .lt(secondElder)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned for the less-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("animals younger than the requested age")
                            .allMatch(animal -> animal.getAge() < secondElder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting mapped superclass entities with a name LIKE pattern")
    class WhenTheNameLikeSelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return only entities whose names match the requested pattern")
        void shouldReturnOnlyMatchingEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);
            String name = animals.getFirst().getName();

            try {
                // When
                List<Animal> result = template.select(Animal.class)
                        .where("name")
                        .like(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned for the LIKE filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("animals whose names match the requested pattern")
                            .allMatch(animal -> animal.getName().contains(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting mapped superclass entities by name membership")
    class WhenTheNameMembershipSelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return only entities whose names are in the requested set")
        void shouldReturnOnlyMatchingEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);
            List<String> names = List.of(animals.getFirst().getName());

            try {
                // When
                List<Animal> result = template.select(Animal.class)
                        .where("name")
                        .in(names)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned for the in filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("animals whose names are included in the requested set")
                            .allMatch(animal -> animal.getName().equals(animals.getFirst().getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting mapped superclass entities by an age range")
    class WhenTheAgeRangeSelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return only entities whose age falls within the requested range")
        void shouldReturnOnlyMatchingEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);
            int minimumAge = animals.stream()
                    .map(Animal::getAge)
                    .sorted()
                    .findFirst()
                    .orElse(0);
            int upperBound = minimumAge + 10;

            try {
                // When
                List<Animal> result = template.select(Animal.class)
                        .where("age")
                        .between(minimumAge, upperBound)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned for the between filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("animals whose age falls within the requested range")
                            .allMatch(animal -> animal.getAge() >= minimumAge && animal.getAge() <= upperBound);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting mapped superclass entities with pagination")
    class WhenThePaginatedSelection {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return the filtered entities within the requested page")
        void shouldReturnOnlyMatchingEntities(List<Animal> animals) {

            // Given
            insertAnimals(animals);
            int secondOlder = animals.stream()
                    .mapToInt(Animal::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            try {
                // When
                List<Animal> result = template.select(Animal.class)
                        .where("age")
                        .gt(secondOlder)
                        .skip(0)
                        .limit(10)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("animals returned for the paginated greater-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("animals in the requested page that satisfy the age filter")
                            .allMatch(animal -> animal.getAge() > secondOlder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    private void insertAnimals(List<Animal> animals) {
        animals.forEach(template::insert);
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported mapped superclass select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
