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
import ee.jakarta.tck.nosql.entities.Beer;
import ee.jakarta.tck.nosql.entities.Drink;
import ee.jakarta.tck.nosql.factories.DrinkListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("Deleting inheritance hierarchies through the template")
public class DeleteTemplateInheritanceTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When deleting inherited entities without conditions")
    class WhenTheDeletionHasNoCondition {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should delete all persisted inherited entities")
        void shouldDeleteAllPersistedInheritedEntities(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Drink.class)
                        .execute();

                // Then
                assertThat(template.select(Drink.class)
                        .result())
                        .as("all persisted inherited entities after deleting without conditions")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting inherited entities through an equality condition")
    class WhenTheDeletionUsesEqualityCondition {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should delete inherited entities matching the selected value")
        void shouldDeleteInheritedEntitiesMatchingTheSelectedValue(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);
            var alcoholPercentage = entities.getFirst().getAlcoholPercentage();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Drink.class)
                        .where("alcoholPercentage")
                        .eq(alcoholPercentage)
                        .execute();

                // Then
                assertThat(template.select(Drink.class)
                        .where("alcoholPercentage")
                        .eq(alcoholPercentage)
                        .result())
                        .as("inherited entities matching the deleted equality value")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting inherited entities through comparison conditions")
    class WhenTheDeletionUsesComparisonCondition {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should delete inherited entities greater than the reference value")
        void shouldDeleteInheritedEntitiesGreaterThanTheReferenceValue(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);
            var alcoholPercentage = entities.getFirst().getAlcoholPercentage();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Drink.class)
                        .where("alcoholPercentage")
                        .gt(alcoholPercentage)
                        .execute();

                // Then
                assertThat(template.select(Drink.class)
                        .where("alcoholPercentage")
                        .gt(alcoholPercentage)
                        .result())
                        .as("inherited entities greater than the deleted comparison value")
                        .isEmpty();
            });
        }

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should delete inherited entities less than the reference value")
        void shouldDeleteInheritedEntitiesLessThanTheReferenceValue(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);
            var alcoholPercentage = entities.getFirst().getAlcoholPercentage();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Drink.class)
                        .where("alcoholPercentage")
                        .lt(alcoholPercentage)
                        .execute();

                // Then
                assertThat(template.select(Drink.class)
                        .where("alcoholPercentage")
                        .lt(alcoholPercentage)
                        .result())
                        .as("inherited entities less than the deleted comparison value")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting inherited entities through membership conditions")
    class WhenTheDeletionUsesMembershipCondition {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should delete inherited entities matching the selected values")
        void shouldDeleteInheritedEntitiesMatchingTheSelectedValues(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);
            var alcoholPercentages = List.of(entities.getFirst().getAlcoholPercentage());

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Drink.class)
                        .where("alcoholPercentage")
                        .in(alcoholPercentages)
                        .execute();

                // Then
                assertThat(template.select(Drink.class)
                        .where("alcoholPercentage")
                        .in(alcoholPercentages)
                        .result())
                        .as("inherited entities matching the deleted membership values")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting inherited entities through range conditions")
    class WhenTheDeletionUsesRangeCondition {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should delete inherited entities within the selected range")
        void shouldDeleteInheritedEntitiesWithinTheSelectedRange(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);
            var startPercentage = entities.getFirst().getAlcoholPercentage();
            var endPercentage = startPercentage + 5;

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Drink.class)
                        .where("alcoholPercentage")
                        .between(startPercentage, endPercentage)
                        .execute();

                // Then
                assertThat(template.select(Drink.class)
                        .where("alcoholPercentage")
                        .between(startPercentage, endPercentage)
                        .result())
                        .as("inherited entities within the deleted range")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting inherited entities through combined conditions")
    class WhenTheDeletionUsesCompositeCondition {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should delete inherited entities matching every selected condition")
        void shouldDeleteInheritedEntitiesMatchingEverySelectedCondition(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);
            var alcoholPercentage = entities.getFirst().getAlcoholPercentage();
            var name = entities.getFirst().getName();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Drink.class)
                        .where("alcoholPercentage")
                        .gt(alcoholPercentage)
                        .and("name")
                        .eq(name)
                        .execute();

                // Then
                assertThat(template.select(Drink.class)
                        .where("alcoholPercentage")
                        .gt(alcoholPercentage)
                        .and("name")
                        .eq(name)
                        .result())
                        .as("inherited entities matching the deleted combined condition")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting a single subtype from the inheritance hierarchy")
    class WhenTheDeletionTargetsASubtype {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should keep only non-targeted subtypes in the result")
        void shouldKeepOnlyNonTargetedSubtypesInTheResult(List<Drink> entities) {
            // Given
            entities.forEach(template::insert);

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Beer.class)
                        .execute();

                // Then
                var result = template.select(Drink.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("remaining inherited entities after deleting the targeted subtype")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("remaining inherited entities should exclude the targeted subtype")
                            .allMatch(drink -> !Beer.class.equals(drink.getClass()));
                });
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
