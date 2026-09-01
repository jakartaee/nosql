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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The query execution exploring negated select filters")
public class SelectNegateTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting entities with a negated name equality filter")
    class WhenTheNegatedNameEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names differ from the requested name")
        void shouldReturnOnlyNonMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String name = entities.getFirst().getName();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name")
                        .not().eq(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated equality filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names differ from the requested name")
                            .allMatch(person -> !person.getName().equals(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with a negated age greater-than filter")
    class WhenTheNegatedAgeGreaterThanSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities that are not older than the requested age")
        void shouldReturnOnlyNonMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = sortedAgeAt(entities, 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .not().gt(secondElder)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated greater-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people who are not older than the requested age")
                            .allMatch(person -> person.getAge() <= secondElder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with a negated age less-than filter")
    class WhenTheNegatedAgeLessThanSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities that are not younger than the requested age")
        void shouldReturnOnlyNonMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = sortedAgeAt(entities, 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .not().lt(secondElder)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated less-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people who are not younger than the requested age")
                            .allMatch(person -> person.getAge() >= secondElder);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with a negated name LIKE filter")
    class WhenTheNegatedNameLikeSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names do not match the requested pattern")
        void shouldReturnOnlyNonMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String name = entities.getFirst().getName();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name")
                        .not().like(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated LIKE filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names do not match the requested pattern")
                            .allMatch(person -> !person.getName().contains(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with a negated name membership filter")
    class WhenTheNegatedNameMembershipSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names are not in the requested set")
        void shouldReturnOnlyNonMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            List<String> names = List.of(entities.getFirst().getName());

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name")
                        .not().in(names)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated in filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names are not in the requested set")
                            .allMatch(person -> !person.getName().equals(entities.getFirst().getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with a negated age range filter")
    class WhenTheNegatedAgeRangeSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose age falls outside the requested range")
        void shouldReturnOnlyNonMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int secondElder = sortedAgeAt(entities, 1);
            int upperBound = secondElder + 5;

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .not().between(secondElder, upperBound)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated between filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose age falls outside the requested range")
                            .allMatch(person -> person.getAge() < secondElder || person.getAge() > upperBound);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with negated filters composed by and")
    class WhenTheNegatedAndSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities that satisfy both negated conditions")
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
                        .not().gt(age)
                        .and("name")
                        .not().eq(name)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated AND filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people that satisfy both negated conditions")
                            .allMatch(person -> person.getAge() <= age && !person.getName().equals(name));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities with negated filters composed by or")
    class WhenTheNegatedOrSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities that satisfy at least one negated condition")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            Person secondElder = oldestPerson(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age")
                        .not().gt(secondElder.getAge())
                        .or("name")
                        .not().eq(secondElder.getName())
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the negated OR filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people that satisfy at least one negated condition")
                            .allMatch(person -> person.getAge() <= secondElder.getAge()
                                    || !person.getName().equals(secondElder.getName()));
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

    private Person oldestPerson(List<Person> entities) {
        return entities.stream()
                .sorted(Comparator.comparing(Person::getAge))
                .skip(entities.size() - 1L)
                .findFirst()
                .orElseThrow();
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported negated select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
