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
package ee.jakarta.tck.nosql.delete;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Deleting entities through the template")
public class DeleteTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When deleting entities without conditions")
    class WhenTheDeletionHasNoCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete all persisted entities")
        void shouldDeleteAllPersistedEntities(List<Person> entities) {
            // Given
            entities.forEach(template::insert);

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .result())
                        .as("all persisted entities after deleting without conditions")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through an equality condition")
    class WhenTheDeletionUsesEqualityCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the selected value")
        void shouldDeleteEntitiesMatchingTheSelectedValue(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var name = entities.getFirst().getName();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("name")
                        .eq(name)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("name")
                        .eq(name)
                        .result())
                        .as("entities matching the deleted equality value")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through comparison conditions")
    class WhenTheDeletionUsesComparisonCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities greater than the reference value")
        void shouldDeleteEntitiesGreaterThanTheReferenceValue(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var age = entities.getFirst().getAge();

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
        @DisplayName("Should delete entities less than the reference value")
        void shouldDeleteEntitiesLessThanTheReferenceValue(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var age = entities.getFirst().getAge();

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
    }

    @Nested
    @DisplayName("When deleting entities through membership conditions")
    class WhenTheDeletionUsesMembershipCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching the selected values")
        void shouldDeleteEntitiesMatchingTheSelectedValues(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var names = List.of(entities.getFirst().getName());

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("name")
                        .in(names)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("name")
                        .in(names)
                        .result())
                        .as("entities matching the deleted membership values")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting entities through range conditions")
    class WhenTheDeletionUsesRangeCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities within the selected range")
        void shouldDeleteEntitiesWithinTheSelectedRange(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var startAge = entities.getFirst().getAge();
            var endAge = startAge + 5;

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
    @DisplayName("When deleting entities through combined conditions")
    class WhenTheDeletionUsesCompositeCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should delete entities matching every selected condition")
        void shouldDeleteEntitiesMatchingEverySelectedCondition(List<Person> entities) {
            // Given
            entities.forEach(template::insert);
            var age = entities.getFirst().getAge();
            var name = entities.getFirst().getName();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Person.class)
                        .where("age")
                        .gt(age)
                        .and("name")
                        .eq(name)
                        .execute();

                // Then
                assertThat(template.select(Person.class)
                        .where("age")
                        .gt(age)
                        .and("name")
                        .eq(name)
                        .result())
                        .as("entities matching the deleted combined condition")
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
