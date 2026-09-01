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

@DisplayName("The query execution of fluent select basic operations")
public class SelectBasicOperationsTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting entities by identifier equality")
    class WhenTheIdentifierEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities with the requested identifier")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String id = entities.getFirst().getId();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("id").eq(id)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the identifier equality filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people matching the requested identifier")
                            .allMatch(person -> person.getId().equals(id));
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
            int age = secondYoungestAge(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age").gt(age)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the greater-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people older than the requested age")
                            .allMatch(person -> person.getAge() > age);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by age greater than or equal to a threshold")
    class WhenTheAgeGreaterThanOrEqualToSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities at least as old as the requested age")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int age = secondYoungestAge(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age").gte(age)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the greater-than-or-equal filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people at least as old as the requested age")
                            .allMatch(person -> person.getAge() >= age);
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
            int age = secondOldestAge(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age").lt(age)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the less-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people younger than the requested age")
                            .allMatch(person -> person.getAge() < age);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by age less than or equal to a threshold")
    class WhenTheAgeLessThanOrEqualToSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities no older than the requested age")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            int age = secondOldestAge(entities);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age").lte(age)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the less-than-or-equal filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people no older than the requested age")
                            .allMatch(person -> person.getAge() <= age);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by identifier membership")
    class WhenTheIdentifierMembershipSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose identifiers are in the requested set")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            List<String> ids = entities.stream().map(Person::getId).limit(3).toList();

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("id").in(ids)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the in filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose identifiers are included in the requested set")
                            .allMatch(person -> ids.contains(person.getId()));
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
            int lowerBound = sortedAgeAt(entities, 1);
            int upperBound = sortedAgeAt(entities, 3);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("age").between(lowerBound, upperBound)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the between filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose age falls within the requested range")
                            .allMatch(person -> person.getAge() <= upperBound && person.getAge() >= lowerBound);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by a contained name fragment")
    class WhenTheNameContainsSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names contain the requested fragment")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String namePart = entities.getFirst().getName().substring(1, 3);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name").contains(namePart)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the contains filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names contain the requested fragment")
                            .allMatch(person -> person.getName().contains(namePart));
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
            String namePart = entities.getFirst().getName().substring(1, 3);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name").like("%" + namePart + "%")
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the LIKE filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names match the requested LIKE pattern")
                            .allMatch(person -> person.getName().contains(namePart));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by a name prefix")
    class WhenTheNamePrefixSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names start with the requested prefix")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String prefix = entities.getFirst().getName().substring(0, 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name").startsWith(prefix)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the starts-with filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names start with the requested prefix")
                            .allMatch(person -> person.getName().startsWith(prefix));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting entities by a name suffix")
    class WhenTheNameSuffixSelection {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return only entities whose names end with the requested suffix")
        void shouldReturnOnlyMatchingEntities(List<Person> entities) {

            // Given
            insertPeople(entities);
            String suffix = entities.getFirst().getName()
                    .substring(entities.getFirst().getName().length() - 1);

            try {
                // When
                List<Person> result = template.select(Person.class)
                        .where("name").endsWith(suffix)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("people returned for the ends-with filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("people whose names end with the requested suffix")
                            .allMatch(person -> person.getName().endsWith(suffix));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    private void insertPeople(List<Person> entities) {
        entities.forEach(template::insert);
    }

    private int secondYoungestAge(List<Person> entities) {
        return sortedAgeAt(entities, 1);
    }

    private int secondOldestAge(List<Person> entities) {
        return entities.stream()
                .sorted(Comparator.comparing(Person::getAge).reversed())
                .skip(1)
                .findFirst()
                .orElseThrow()
                .getAge();
    }

    private int sortedAgeAt(List<Person> entities, long index) {
        return entities.stream()
                .sorted(Comparator.comparing(Person::getAge))
                .skip(index)
                .findFirst()
                .orElseThrow()
                .getAge();
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
