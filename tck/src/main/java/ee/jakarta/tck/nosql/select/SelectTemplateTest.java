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
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The query execution exploring fluent select operations")
public class SelectTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting entities without filters")
    class WhenTheUnfilteredSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return every inserted entity as a stream")
        void shouldReturnAllEntitiesAsAStream(List<Person> entities) {

            // Given
            insertPeople(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .<Person>stream()
                        .toList();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned as a stream")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("number of people returned as a stream")
                            .hasSize(entities.size());
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return every inserted entity as a result list")
        void shouldReturnAllEntities(List<Person> entities) {

            // Given
            insertPeople(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned without filters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("number of people returned without filters")
                            .hasSize(entities.size());
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting a single entity by identifier")
    class WhenTheSingleResultSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the requested entity")
        void shouldReturnTheMatchingEntity(List<Person> entities) {

            // Given
            insertPeople(entities);
            String id = entities.getFirst().getId();

            try {
                // When
                Optional<Person> result = template.select(Person.class)
                        .where("id").eq(id)
                        .singleResult();

                // Then
                assertThat(result)
                        .as("person returned for the single-result query")
                        .hasValueSatisfying(person -> assertThat(person.getId())
                                .as("identifier of the returned person")
                                .isEqualTo(id));
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When ordering entities by name")
    class WhenTheOrdering {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the distinct names in ascending order")
        void shouldReturnNamesInAscendingOrder(List<Person> entities) {

            // Given
            insertPeople(entities);
            List<String> expectedNames = entities.stream()
                    .map(Person::getName)
                    .sorted()
                    .toList();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .orderBy("name")
                        .asc()
                        .result();
                List<String> names = result.stream().map(Person::getName).distinct().toList();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(names)
                            .as("distinct names returned in ascending order")
                            .isNotEmpty();
                    softly.assertThat(names)
                            .as("ascending ordering of distinct names")
                            .containsExactly(expectedNames.toArray(String[]::new));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the distinct names in descending order")
        void shouldReturnNamesInDescendingOrder(List<Person> entities) {

            // Given
            insertPeople(entities);
            List<String> expectedNames = entities.stream()
                    .map(Person::getName)
                    .sorted(Comparator.reverseOrder())
                    .toList();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .orderBy("name")
                        .desc()
                        .result();
                List<String> names = result.stream().map(Person::getName).distinct().toList();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(names)
                            .as("distinct names returned in descending order")
                            .isNotEmpty();
                    softly.assertThat(names)
                            .as("descending ordering of distinct names")
                            .containsExactly(expectedNames.toArray(String[]::new));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return filtered entities ordered by name")
        void shouldReturnFilteredEntitiesInAscendingOrder(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = sortedAgeAt(entities, 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .gt(secondElder)
                        .orderBy("name")
                        .asc()
                        .result();
                List<String> names = result.stream().map(Person::getName).toList();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("filtered people returned for the ordered query")
                            .isNotEmpty();
                    softly.assertThat(names)
                            .as("ordering of filtered people by name")
                            .isSorted();
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When limiting the number of selected entities")
    class WhenTheLimit {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only the requested number of entities")
        void shouldReturnOnlyTheRequestedNumberOfEntities(List<Person> entities) {

            // Given
            insertPeople(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .limit(3)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned after applying the limit")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("number of people returned after applying the limit")
                            .hasSize(3);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When skipping selected entities")
    class WhenTheSkip {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only the remaining entities after the skipped entries")
        void shouldReturnTheRemainingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .skip(2)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned after skipping entries")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("number of people returned after skipping entries")
                            .hasSize(entities.size() - 2);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by name equality")
    class WhenTheNameEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities with the requested name")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String name = entities.getFirst().getName();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name")
                        .eq(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the name equality filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people matching the requested name")
                            .allMatch(person -> person.getName().equals(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by age greater than a threshold")
    class WhenTheAgeGreaterThanSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities older than the requested age")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = sortedAgeAt(entities, 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .gt(secondElder)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the greater-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people older than the requested age")
                            .allMatch(person -> person.getAge() > secondElder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by age less than a threshold")
    class WhenTheAgeLessThanSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities younger than the requested age")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = sortedAgeAt(entities, 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .lt(secondElder)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the less-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people younger than the requested age")
                            .allMatch(person -> person.getAge() < secondElder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with a name LIKE pattern")
    class WhenTheNameLikeSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names match the requested pattern")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String name = entities.getFirst().getName();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name")
                        .like(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the LIKE filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names match the requested pattern")
                            .allMatch(person -> person.getName().contains(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by name membership")
    class WhenTheNameMembershipSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names are in the requested set")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            List<String> names = List.of(entities.getFirst().getName());

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name")
                        .in(names)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the in filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names are included in the requested set")
                            .allMatch(person -> person.getName().equals(entities.getFirst().getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by an age range")
    class WhenTheAgeRangeSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose age falls within the requested range")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = ageAfterSkippingFirstEntity(entities);
            int upperBound = secondElder + 5;

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .between(secondElder, upperBound)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the between filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose age falls within the requested range")
                            .allMatch(person -> person.getAge() >= secondElder && person.getAge() <= upperBound);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with pagination")
    class WhenThePaginatedSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the filtered entities within the requested page")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = sortedAgeAt(entities, 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .gt(secondElder)
                        .skip(0)
                        .limit(10)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the paginated filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people in the requested page that satisfy the age filter")
                            .allMatch(person -> person.getAge() > secondElder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with filters composed by and")
    class WhenTheAndSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities that satisfy both conditions")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            Person secondElder = oldestPerson(entities);
            int age = secondElder.getAge() - 1;
            String name = secondElder.getName();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .gt(age)
                        .and("name")
                        .eq(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the AND filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people that satisfy both conditions")
                            .allMatch(person -> person.getAge() > age && person.getName().equals(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with filters composed by or")
    class WhenTheOrSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities that satisfy at least one condition")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            Person secondElder = oldestPerson(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .gt(secondElder.getAge())
                        .or("name")
                        .eq(secondElder.getName())
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the OR filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people that satisfy at least one condition")
                            .allMatch(person -> person.getAge() > secondElder.getAge()
                                    || person.getName().equals(secondElder.getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    private void insertPeople(List<Person> entities) {
        entities.forEach(template::insert);
    }

    private int sortedAgeAt(List<Person> entities, long index) {
        return entities.stream()
                .map(Person::getAge)
                .sorted()
                .skip(index)
                .findFirst()
                .orElseThrow();
    }

    private int ageAfterSkippingFirstEntity(List<Person> entities) {
        return entities.stream()
                .mapToInt(Person::getAge)
                .skip(1)
                .findFirst()
                .orElseThrow();
    }

    private Person oldestPerson(List<Person> entities) {
        return entities.stream()
                .sorted(Comparator.comparing(Person::getAge))
                .skip(entities.size() - 1L)
                .findFirst()
                .orElseThrow();
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
