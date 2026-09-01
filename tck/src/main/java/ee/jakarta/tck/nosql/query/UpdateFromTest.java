/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
import ee.jakarta.tck.nosql.entities.Fruit;
import ee.jakarta.tck.nosql.factories.FruitListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The Jakarta Query integration test using update")
class UpdateFromTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When bulk update is executed")
    class WhenTheBulkUpdateIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update every entity with a literal value")
        void shouldUpdateEveryEntityWithALiteralValue(List<Fruit> fruits) {
            try {
                template.insert(fruits);

                template.query("UPDATE Fruit SET quantity = 19").executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned after the literal-value update")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("updated values after the literal-value update")
                            .allMatch(fruit -> fruit.getQuantity() == 19);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update every entity with a bound value")
        void shouldUpdateEveryEntityWithABoundValue(List<Fruit> fruits) {
            try {
                template.insert(fruits);

                template.query("UPDATE Fruit SET quantity = :quantity")
                        .bind("quantity", 19)
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned after the bound-value update")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("updated values after the bound-value update")
                            .allMatch(fruit -> fruit.getQuantity() == 19);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When an identity condition is used")
    class WhenTheIdentityConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update the entity matching the equality condition")
        void shouldUpdateTheEntityMatchingTheEqualityCondition(List<Fruit> fruits) {
            try {
                template.insert(fruits);

                template.query("UPDATE Fruit SET quantity = :quantity WHERE id = :id")
                        .bind("quantity", 19)
                        .bind("id", fruits.getFirst().getId())
                        .executeUpdate();

                Optional<Fruit> result = template.query("FROM Fruit where id = :id")
                        .bind("id", fruits.getFirst().getId())
                        .singleResult();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("result returned after the equality update")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("type returned after the equality update")
                            .containsInstanceOf(Fruit.class);
                    softly.assertThat(result.map(Fruit::getQuantity))
                            .as("updated value after the equality update")
                            .contains(19L);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update entities matching the inequality condition")
        void shouldUpdateEntitiesMatchingTheInequalityCondition(List<Fruit> fruits) {
            try {
                template.insert(fruits);

                template.query("UPDATE Fruit SET quantity = :quantity WHERE id <> :id")
                        .bind("quantity", 19)
                        .bind("id", fruits.getFirst().getId())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit where id <> :id")
                        .bind("id", fruits.getFirst().getId())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned after the inequality update")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("updated values after the inequality update")
                            .allMatch(fruit -> fruit.getQuantity() == 19);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update entities in the selected set")
        void shouldUpdateEntitiesInTheSelectedSet(List<Fruit> fruits) {
            try {
                template.insert(fruits);

                template.query("UPDATE Fruit SET quantity = :quantity WHERE id IN (:ids)")
                        .bind("quantity", 19)
                        .bind("ids", fruits.stream().map(Fruit::getId).toList())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit where id IN (:ids)")
                        .bind("ids", fruits.stream().map(Fruit::getId).toList())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned after the membership update")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("updated values after the membership update")
                            .allMatch(fruit -> fruit.getQuantity() == 19);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When a range condition is used")
    class WhenTheRangeConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update selected values for entities greater than the bound value")
        void shouldUpdateSelectedValuesForEntitiesGreaterThanTheBoundValue(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity > :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit WHERE quantity > :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertThat(result)
                        .as("updated values after the greater-than condition")
                        .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update selected values for entities greater than or equal to the bound value")
        void shouldUpdateSelectedValuesForEntitiesGreaterThanOrEqualToTheBoundValue(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity >= :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit WHERE quantity >= :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertThat(result)
                        .as("updated values after the greater-than-or-equal condition")
                        .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update selected values for entities less than the bound value")
        void shouldUpdateSelectedValuesForEntitiesLessThanTheBoundValue(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity < :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit WHERE quantity < :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertThat(result)
                        .as("updated values after the less-than condition")
                        .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should update selected values for entities less than or equal to the bound value")
        void shouldUpdateSelectedValuesForEntitiesLessThanOrEqualToTheBoundValue(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity <= :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit WHERE quantity <= :quantity")
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertThat(result)
                        .as("updated values after the less-than-or-equal condition")
                        .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    private void assertUnsupportedOperation(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("unsupported update query portability handling")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
