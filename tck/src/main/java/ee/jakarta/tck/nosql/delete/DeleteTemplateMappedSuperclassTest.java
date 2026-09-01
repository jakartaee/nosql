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
import ee.jakarta.tck.nosql.entities.Animal;
import ee.jakarta.tck.nosql.factories.AnimalListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Deleting mapped superclass entities through the template")
public class DeleteTemplateMappedSuperclassTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When deleting mapped superclass entities without conditions")
    class WhenTheDeletionHasNoCondition {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should delete all persisted mapped superclass entities")
        void shouldDeleteAllPersistedMappedSuperclassEntities(List<Animal> entities) {
            // Given
            entities.forEach(template::insert);

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Animal.class)
                        .execute();

                // Then
                assertThat(template.select(Animal.class)
                        .result())
                        .as("all persisted mapped superclass entities after deleting without conditions")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting mapped superclass entities through an equality condition")
    class WhenTheDeletionUsesEqualityCondition {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should delete mapped superclass entities matching the selected value")
        void shouldDeleteMappedSuperclassEntitiesMatchingTheSelectedValue(List<Animal> entities) {
            // Given
            entities.forEach(template::insert);
            var name = entities.getFirst().getName();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Animal.class)
                        .where("name")
                        .eq(name)
                        .execute();

                // Then
                assertThat(template.select(Animal.class)
                        .where("name")
                        .eq(name)
                        .result())
                        .as("mapped superclass entities matching the deleted equality value")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting mapped superclass entities through membership conditions")
    class WhenTheDeletionUsesMembershipCondition {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should delete mapped superclass entities matching the selected values")
        void shouldDeleteMappedSuperclassEntitiesMatchingTheSelectedValues(List<Animal> entities) {
            // Given
            entities.forEach(template::insert);
            var names = List.of(entities.getFirst().getName());

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Animal.class)
                        .where("name")
                        .in(names)
                        .execute();

                // Then
                assertThat(template.select(Animal.class)
                        .where("name")
                        .in(names)
                        .result())
                        .as("mapped superclass entities matching the deleted membership values")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting mapped superclass entities through range conditions")
    class WhenTheDeletionUsesRangeCondition {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should delete mapped superclass entities within the selected range")
        void shouldDeleteMappedSuperclassEntitiesWithinTheSelectedRange(List<Animal> entities) {
            // Given
            entities.forEach(template::insert);
            var startSpecies = entities.getFirst().getSpecies();
            var endSpecies = "Zebra";

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Animal.class)
                        .where("species")
                        .between(startSpecies, endSpecies)
                        .execute();

                // Then
                assertThat(template.select(Animal.class)
                        .where("species")
                        .between(startSpecies, endSpecies)
                        .result())
                        .as("mapped superclass entities within the deleted range")
                        .isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("When deleting mapped superclass entities through combined conditions")
    class WhenTheDeletionUsesCompositeCondition {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should delete mapped superclass entities matching every selected condition")
        void shouldDeleteMappedSuperclassEntitiesMatchingEverySelectedCondition(List<Animal> entities) {
            // Given
            entities.forEach(template::insert);
            var genus = entities.getFirst().getGenus();
            var species = entities.getFirst().getSpecies();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Animal.class)
                        .where("genus")
                        .eq(genus)
                        .and("species")
                        .eq(species)
                        .execute();

                // Then
                assertThat(template.select(Animal.class)
                        .where("genus")
                        .eq(genus)
                        .and("species")
                        .eq(species)
                        .result())
                        .as("mapped superclass entities matching the deleted combined conditions")
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
