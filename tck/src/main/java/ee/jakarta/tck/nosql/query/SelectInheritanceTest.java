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
package ee.jakarta.tck.nosql.query;


import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Animal;
import ee.jakarta.tck.nosql.entities.Coffee;
import ee.jakarta.tck.nosql.entities.Drink;
import ee.jakarta.tck.nosql.factories.AnimalListSupplier;
import ee.jakarta.tck.nosql.factories.DrinkListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The Jakarta Query integration test using select with inheritance")
class SelectInheritanceTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When inheritance selection is executed")
    class WhenTheInheritanceSelectionIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return all entities from the hierarchy")
        void shouldReturnAllEntitiesFromTheHierarchy(List<Drink> entities) {
            template.insert(entities);

            try {
                List<Drink> result = template.query("FROM Drink").result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned from the hierarchy query")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("entity count returned from the hierarchy query")
                            .hasSize(entities.size());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only entities from the requested subtype")
        void shouldReturnOnlyEntitiesFromTheRequestedSubtype(List<Drink> entities) {
            template.insert(entities);

            try {
                List<Drink> result = template.query("FROM Coffee").result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned from the subtype query")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("entity types returned from the subtype query")
                            .allMatch(entity -> entity instanceof Coffee);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When mapped-superclass selection is executed")
    class WhenTheMappedSuperclassSelectionIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(AnimalListSupplier.class)
        @DisplayName("Should return all entities from the mapped superclass")
        void shouldReturnAllEntitiesFromTheMappedSuperclass(List<Animal> entities) {
            template.insert(entities);

            try {
                List<Animal> result = template.query("FROM Animal").result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned from the mapped-superclass query")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("entity count returned from the mapped-superclass query")
                            .hasSize(entities.size());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    private void assertUnsupportedOperation(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("unsupported inheritance query portability handling")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
