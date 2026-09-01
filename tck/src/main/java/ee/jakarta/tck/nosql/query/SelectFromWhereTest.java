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
import ee.jakarta.tck.nosql.entities.Fruit;
import ee.jakarta.tck.nosql.factories.FruitListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The Jakarta Query integration test using select where clause")
public class SelectFromWhereTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When an equality condition is used")
    class WhenTheEqualityConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return matching entities using parameters")
        void shouldReturnMatchingEntitiesUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE name = :name", Fruit.class)
                        .bind("name", sampleFruit.getName())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the equality condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("matched values returned by the equality condition with parameters")
                            .allMatch(fruit -> fruit.getName().equals(sampleFruit.getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return matching entities using literal values")
        void shouldReturnMatchingEntitiesUsingLiteralValues(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE name = '" + sampleFruit.getName() + "'",
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the equality condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("matched values returned by the equality condition with literal values")
                            .allMatch(fruit -> fruit.getName().equals(sampleFruit.getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When an inequality condition is used")
    class WhenTheInequalityConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return non-matching entities using parameters")
        void shouldReturnNonMatchingEntitiesUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE name <> :name", Fruit.class)
                        .bind("name", sampleFruit.getName())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the inequality condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the inequality condition with parameters")
                            .allMatch(fruit -> !fruit.getName().equals(sampleFruit.getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return non-matching entities using literal values")
        void shouldReturnNonMatchingEntitiesUsingLiteralValues(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE name <> '" + sampleFruit.getName() + "'",
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the inequality condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the inequality condition with literal values")
                            .allMatch(fruit -> !fruit.getName().equals(sampleFruit.getName()));
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
        @DisplayName("Should return entities greater than the bound value using parameters")
        void shouldReturnEntitiesGreaterThanTheBoundValueUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity > :quantity", Fruit.class)
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the greater-than condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the greater-than condition with parameters")
                            .allMatch(fruit -> fruit.getQuantity() > sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return entities greater than or equal to the bound value using parameters")
        void shouldReturnEntitiesGreaterThanOrEqualToTheBoundValueUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity >= :quantity", Fruit.class)
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the greater-than-or-equal condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the greater-than-or-equal condition with parameters")
                            .allMatch(fruit -> fruit.getQuantity() >= sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return entities less than the bound value using parameters")
        void shouldReturnEntitiesLessThanTheBoundValueUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity < :quantity", Fruit.class)
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the less-than condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the less-than condition with parameters")
                            .allMatch(fruit -> fruit.getQuantity() < sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return entities less than or equal to the bound value using parameters")
        void shouldReturnEntitiesLessThanOrEqualToTheBoundValueUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity <= :quantity", Fruit.class)
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the less-than-or-equal condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the less-than-or-equal condition with parameters")
                            .allMatch(fruit -> fruit.getQuantity() <= sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return entities greater than the bound value using literal values")
        void shouldReturnEntitiesGreaterThanTheBoundValueUsingLiteralValues(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE quantity > " + sampleFruit.getQuantity(),
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the greater-than condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the greater-than condition with literal values")
                            .allMatch(fruit -> fruit.getQuantity() > sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return entities greater than or equal to the bound value using literal values")
        void shouldReturnEntitiesGreaterThanOrEqualToTheBoundValueUsingLiteralValues(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE quantity >= " + sampleFruit.getQuantity(),
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the greater-than-or-equal condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the greater-than-or-equal condition with literal values")
                            .allMatch(fruit -> fruit.getQuantity() >= sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return entities less than the bound value using literal values")
        void shouldReturnEntitiesLessThanTheBoundValueUsingLiteralValues(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE quantity < " + sampleFruit.getQuantity(),
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the less-than condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the less-than condition with literal values")
                            .allMatch(fruit -> fruit.getQuantity() < sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return entities less than or equal to the bound value using literal values")
        void shouldReturnEntitiesLessThanOrEqualToTheBoundValueUsingLiteralValues(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE quantity <= " + sampleFruit.getQuantity(),
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the less-than-or-equal condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the less-than-or-equal condition with literal values")
                            .allMatch(fruit -> fruit.getQuantity() <= sampleFruit.getQuantity());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When a membership condition is used")
    class WhenTheMembershipConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return matching entities using parameters")
        void shouldReturnMatchingEntitiesUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                var sampleFruit = fruits.getFirst();
                var anotherSampleFruit = fruits.get(1);

                List<Fruit> result = template.typedQuery("FROM Fruit WHERE name IN (:name1, :name2)", Fruit.class)
                        .bind("name1", sampleFruit.getName())
                        .bind("name2", anotherSampleFruit.getName())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the membership condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the membership condition with parameters")
                            .allMatch(fruit -> fruit.getName().equals(sampleFruit.getName())
                                    || fruit.getName().equals(anotherSampleFruit.getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return matching entities using literal values")
        void shouldReturnMatchingEntitiesUsingLiteralValues(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                var sampleFruit = fruits.getFirst();
                var anotherSampleFruit = fruits.get(1);

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE name IN ('" + sampleFruit.getName()
                                        + "', '" + anotherSampleFruit.getName() + "')",
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the membership condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the membership condition with literal values")
                            .allMatch(fruit -> fruit.getName().equals(sampleFruit.getName())
                                    || fruit.getName().equals(anotherSampleFruit.getName()));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    private void assertUnsupportedOperation(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("unsupported select query portability handling")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
