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

@DisplayName("The Jakarta Query integration test using select with where composite condition (AND, OR)")
public class SelectFromWhereCompositeConditionTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When a composite condition uses conjunction")
    class WhenTheConjunctionConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return matching entities using parameters")
        void shouldReturnMatchingEntitiesUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.getFirst();

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE name = :name AND quantity = :quantity",
                                Fruit.class)
                        .bind("name", sampleFruit.getName())
                        .bind("quantity", sampleFruit.getQuantity())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the conjunction condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the conjunction condition with parameters")
                            .allMatch(fruit -> fruit.getName().equals(sampleFruit.getName())
                                    && fruit.getQuantity().equals(sampleFruit.getQuantity()));
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

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE name = '" + sampleFruit.getName() + "' AND quantity = "
                                        + sampleFruit.getQuantity(),
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the conjunction condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the conjunction condition with literal values")
                            .allMatch(fruit -> fruit.getName().equals(sampleFruit.getName())
                                    && fruit.getQuantity().equals(sampleFruit.getQuantity()));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When a composite condition uses disjunction")
    class WhenTheDisjunctionConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should return matching entities using parameters")
        void shouldReturnMatchingEntitiesUsingParameters(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sampleFruit = fruits.get(0);
                Fruit anotherSampleFruit = fruits.get(1);

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE name = :name1 OR name = :name2",
                                Fruit.class)
                        .bind("name1", sampleFruit.getName())
                        .bind("name2", anotherSampleFruit.getName())
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the disjunction condition with parameters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the disjunction condition with parameters")
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
                Fruit sampleFruit = fruits.get(0);
                Fruit anotherSampleFruit = fruits.get(1);

                List<Fruit> result = template.typedQuery(
                                "FROM Fruit WHERE name = '" + sampleFruit.getName() + "' OR name = '"
                                        + anotherSampleFruit.getName() + "'",
                                Fruit.class)
                        .result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entities returned by the disjunction condition with literal values")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("returned values from the disjunction condition with literal values")
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
                .as("unsupported composite select query portability handling")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
