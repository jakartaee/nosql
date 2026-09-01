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

@DisplayName("The Jakarta Query integration test using delete")
class DeleteFromTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When bulk deletion is executed")
    class WhenTheBulkDeletionIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should remove every entity")
        void shouldRemoveEveryEntity(List<Fruit> fruits) {
            try {
                template.insert(fruits);

                template.query("DELETE FROM Fruit").executeUpdate();

                var result = template.query("FROM Fruit").stream();

                assertThat(result)
                        .as("remaining entities after bulk deletion")
                        .isEmpty();
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When an equality condition is used")
    class WhenTheEqualityConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should remove matching entities")
        void shouldRemoveMatchingEntities(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();

                template.query("DELETE FROM Fruit WHERE name = :name")
                        .bind("name", sample.getName())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the equality condition")
                        .allMatch(fruit -> !fruit.getName().equals(sample.getName()));
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
        @DisplayName("Should retain only entities matching the excluded value")
        void shouldRetainOnlyEntitiesMatchingTheExcludedValue(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();

                template.typedQuery("DELETE FROM Fruit WHERE name <> :name", Fruit.class)
                        .bind("name", sample.getName())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the inequality condition")
                        .allMatch(fruit -> fruit.getName().equals(sample.getName()));
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
        @DisplayName("Should retain entities up to the bound value after a greater-than delete")
        void shouldRetainEntitiesUpToTheBoundValueAfterAGreaterThanDelete(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();

                template.typedQuery("DELETE FROM Fruit WHERE quantity > :quantity", Fruit.class)
                        .bind("quantity", sample.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the greater-than delete")
                        .allMatch(fruit -> fruit.getQuantity() <= sample.getQuantity());
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should retain entities below the bound value after a greater-than-or-equal delete")
        void shouldRetainEntitiesBelowTheBoundValueAfterAGreaterThanOrEqualDelete(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();

                template.typedQuery("DELETE FROM Fruit WHERE quantity >= :quantity", Fruit.class)
                        .bind("quantity", sample.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the greater-than-or-equal delete")
                        .allMatch(fruit -> fruit.getQuantity() < sample.getQuantity());
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should retain entities from the bound value upward after a less-than delete")
        void shouldRetainEntitiesFromTheBoundValueUpwardAfterALessThanDelete(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();

                template.typedQuery("DELETE FROM Fruit WHERE quantity < :quantity", Fruit.class)
                        .bind("quantity", sample.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the less-than delete")
                        .allMatch(fruit -> fruit.getQuantity() >= sample.getQuantity());
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should retain entities above the bound value after a less-than-or-equal delete")
        void shouldRetainEntitiesAboveTheBoundValueAfterALessThanOrEqualDelete(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();

                template.typedQuery("DELETE FROM Fruit WHERE quantity <= :quantity", Fruit.class)
                        .bind("quantity", sample.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the less-than-or-equal delete")
                        .allMatch(fruit -> fruit.getQuantity() > sample.getQuantity());
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
        @DisplayName("Should remove entities in the selected set")
        void shouldRemoveEntitiesInTheSelectedSet(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                var sample1 = fruits.getFirst();
                var sample2 = fruits.get(1);

                template.typedQuery("DELETE FROM Fruit WHERE name IN (:name1, :name2)", Fruit.class)
                        .bind("name1", sample1.getName())
                        .bind("name2", sample2.getName())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the membership condition")
                        .allMatch(fruit -> !fruit.getName().equals(sample1.getName())
                                && !fruit.getName().equals(sample2.getName()));
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When a composite condition uses conjunction")
    class WhenTheConjunctionConditionIsUsed {

        @ParameterizedTest
        @ArgumentsSource(FruitListSupplier.class)
        @DisplayName("Should remove entities matching all predicates")
        void shouldRemoveEntitiesMatchingAllPredicates(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();

                template.typedQuery(
                                "DELETE FROM Fruit WHERE name = :name AND quantity = :quantity",
                                Fruit.class)
                        .bind("name", sample.getName())
                        .bind("quantity", sample.getQuantity())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the conjunction condition")
                        .allMatch(fruit -> !(fruit.getName().equals(sample.getName())
                                && fruit.getQuantity().equals(sample.getQuantity())));
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
        @DisplayName("Should remove entities matching any predicate")
        void shouldRemoveEntitiesMatchingAnyPredicate(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample1 = fruits.get(0);
                Fruit sample2 = fruits.get(1);

                template.typedQuery("DELETE FROM Fruit WHERE name = :name1 OR name = :name2", Fruit.class)
                        .bind("name1", sample1.getName())
                        .bind("name2", sample2.getName())
                        .executeUpdate();

                List<Fruit> result = template.query("FROM Fruit").result();

                assertThat(result)
                        .as("remaining entities after the disjunction condition")
                        .allMatch(fruit -> !fruit.getName().equals(sample1.getName())
                                && !fruit.getName().equals(sample2.getName()));
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    private void assertUnsupportedOperation(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("unsupported delete query portability handling")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
