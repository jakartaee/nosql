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

@DisplayName("The query execution of fluent select basic operations with count")
public class SelectBasicOperationsCountTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When counting entities by identifier equality")
    class WhenTheIdentifierEqualityCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities with the requested identifier")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            String id = entities.getFirst().getId();
            long expected = entities.stream().filter(person -> person.getId().equals(id)).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("id").eq(id)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the identifier equality filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities older than a threshold")
    class WhenTheAgeGreaterThanCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities older than the requested age")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            int age = sortedAgeAt(entities, 1);
            long expected = entities.stream().filter(person -> person.getAge() > age).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("age").gt(age)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the greater-than filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities at least as old as a threshold")
    class WhenTheAgeGreaterThanOrEqualToCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities at least as old as the requested age")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            int age = sortedAgeAt(entities, 1);
            long expected = entities.stream().filter(person -> person.getAge() >= age).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("age").gte(age)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the greater-than-or-equal filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities younger than a threshold")
    class WhenTheAgeLessThanCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities younger than the requested age")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            int age = reversedSortedAgeAt(entities, 1);
            long expected = entities.stream().filter(person -> person.getAge() < age).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("age").lt(age)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the less-than filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities no older than a threshold")
    class WhenTheAgeLessThanOrEqualToCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities no older than the requested age")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            int age = reversedSortedAgeAt(entities, 1);
            long expected = entities.stream().filter(person -> person.getAge() <= age).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("age").lte(age)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the less-than-or-equal filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities by identifier membership")
    class WhenTheIdentifierMembershipCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities whose identifiers are in the requested set")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            List<String> ids = entities.stream().map(Person::getId).limit(3).toList();
            long expected = entities.stream().filter(person -> ids.contains(person.getId())).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("id").in(ids)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the in filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities by an age range")
    class WhenTheAgeRangeCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities whose age falls within the requested range")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            int lowerBound = sortedAgeAt(entities, 1);
            int upperBound = sortedAgeAt(entities, 3);
            long expected = entities.stream()
                    .filter(person -> person.getAge() <= upperBound && person.getAge() >= lowerBound)
                    .count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("age").between(lowerBound, upperBound)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the between filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities by a contained name fragment")
    class WhenTheNameContainsCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities whose names contain the requested fragment")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            String namePart = entities.getFirst().getName().substring(1, 3);
            long expected = entities.stream().filter(person -> person.getName().contains(namePart)).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("name").contains(namePart)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the contains filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities with a name LIKE pattern")
    class WhenTheNameLikeCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities whose names match the requested pattern")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            String namePart = entities.getFirst().getName().substring(1, 3);
            long expected = entities.stream().filter(person -> person.getName().contains(namePart)).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("name").like("%" + namePart + "%")
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the LIKE filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities by a name prefix")
    class WhenTheNamePrefixCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities whose names start with the requested prefix")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            String prefix = entities.getFirst().getName().substring(0, 1);
            long expected = entities.stream().filter(person -> person.getName().startsWith(prefix)).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("name").startsWith(prefix)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the starts-with filter")
                        .isEqualTo(expected);
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When counting entities by a name suffix")
    class WhenTheNameSuffixCount {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should return the number of entities whose names end with the requested suffix")
        void shouldReturnTheMatchingCount(List<Person> entities) {

            // Given
            insertPeople(entities);
            String suffix = entities.getFirst().getName().substring(0, 1);
            long expected = entities.stream().filter(person -> person.getName().endsWith(suffix)).count();

            try {
                // When
                long count = template.select(Person.class)
                        .where("name").endsWith(suffix)
                        .count();

                // Then
                assertThat(count)
                        .as("number of people matching the ends-with filter")
                        .isEqualTo(expected);
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
                .sorted(Comparator.comparing(Person::getAge))
                .skip(index)
                .findFirst()
                .orElseThrow()
                .getAge();
    }

    private int reversedSortedAgeAt(List<Person> entities, long index) {
        return entities.stream()
                .sorted(Comparator.comparing(Person::getAge).reversed())
                .skip(index)
                .findFirst()
                .orElseThrow()
                .getAge();
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported select count operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
