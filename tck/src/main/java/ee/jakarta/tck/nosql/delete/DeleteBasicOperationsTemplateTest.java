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
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Deleting entities with fluent template operations")
public class DeleteBasicOperationsTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When deleting entities through fluent identifier equality")
    class WhenTheDeletionUsesIdentifierEqualityCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the selected identifier")
        void shouldDeleteEntitiesMatchingTheSelectedIdentifier(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var id = entities.getFirst().getId();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("id")
                        .eq(id)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("id")
                        .eq(id)
                        .result())
                        .as("entities matching the deleted identifier")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through fluent comparison conditions")
    class WhenTheDeletionUsesComparisonCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities greater than the reference value")
        void shouldDeleteEntitiesGreaterThanTheReferenceValue(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var age = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge))
                    .skip(1)
                    .findFirst()
                    .orElseThrow()
                    .getAge();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("age")
                        .gt(age)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("age")
                        .gt(age)
                        .result())
                        .as("entities greater than the deleted comparison value")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities greater than or equal to the reference value")
        void shouldDeleteEntitiesGreaterThanOrEqualToTheReferenceValue(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var age = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge))
                    .skip(1)
                    .findFirst()
                    .orElseThrow()
                    .getAge();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("age")
                        .gte(age)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("age")
                        .gte(age)
                        .result())
                        .as("entities greater than or equal to the deleted comparison value")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities less than the reference value")
        void shouldDeleteEntitiesLessThanTheReferenceValue(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var age = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge).reversed())
                    .skip(1)
                    .findFirst()
                    .orElseThrow()
                    .getAge();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("age")
                        .lt(age)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("age")
                        .lt(age)
                        .result())
                        .as("entities less than the deleted comparison value")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities less than or equal to the reference value")
        void shouldDeleteEntitiesLessThanOrEqualToTheReferenceValue(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var age = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge).reversed())
                    .skip(1)
                    .findFirst()
                    .orElseThrow()
                    .getAge();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("age")
                        .lte(age)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("age")
                        .lte(age)
                        .result())
                        .as("entities less than or equal to the deleted comparison value")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through fluent membership conditions")
    class WhenTheDeletionUsesMembershipCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the selected identifiers")
        void shouldDeleteEntitiesMatchingTheSelectedIdentifiers(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var ids = entities.stream()
                    .map(Person::getId)
                    .limit(3)
                    .toList();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("id")
                        .in(ids)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("id")
                        .in(ids)
                        .result())
                        .as("entities matching the deleted identifiers")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through fluent range conditions")
    class WhenTheDeletionUsesRangeCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities within the selected range")
        void shouldDeleteEntitiesWithinTheSelectedRange(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var startAge = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge))
                    .skip(1)
                    .findFirst()
                    .orElseThrow()
                    .getAge();
            var endAge = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge))
                    .skip(3)
                    .findFirst()
                    .orElseThrow()
                    .getAge();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("age")
                        .between(startAge, endAge)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("age")
                        .between(startAge, endAge)
                        .result())
                        .as("entities within the deleted range")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through fluent text conditions")
    class WhenTheDeletionUsesTextSearchCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the contained text")
        void shouldDeleteEntitiesMatchingTheContainedText(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var nameFragment = entities.getFirst().getName().substring(1, 3);

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("name")
                        .contains(nameFragment)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("name")
                        .contains(nameFragment)
                        .result())
                        .as("entities matching the deleted contained text")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the selected pattern")
        void shouldDeleteEntitiesMatchingTheSelectedPattern(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var nameFragment = entities.getFirst().getName().substring(1, 3);
            var pattern = "%" + nameFragment + "%";

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("name")
                        .like(pattern)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("name")
                        .like(pattern)
                        .result())
                        .as("entities matching the deleted pattern")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the selected prefix")
        void shouldDeleteEntitiesMatchingTheSelectedPrefix(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var prefix = entities.getFirst().getName().substring(0, 1);

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("name")
                        .startsWith(prefix)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("name")
                        .startsWith(prefix)
                        .result())
                        .as("entities matching the deleted prefix")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the selected suffix")
        void shouldDeleteEntitiesMatchingTheSelectedSuffix(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var suffix = entities.getFirst().getName().substring(0, 1);

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("name")
                        .endsWith(suffix)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("name")
                        .endsWith(suffix)
                        .result())
                        .as("entities matching the deleted suffix")
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
