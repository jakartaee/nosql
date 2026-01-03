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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("The Jakarta Query integration test using select where clause")
public class SelectFromWhereTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When there is param binder")
    class WhenThereIsParamBinder {

        @ParameterizedTest
        @DisplayName("should test eq")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldEq(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();
                List<Fruit> result = template.typedQuery("FROM Fruit WHERE name = :name", Fruit.class)
                        .bind("name", sample.getName())
                        .result();

                assertThat(result)
                        .isNotEmpty()
                        .allMatch(fruit -> fruit.getName().equals(sample.getName()));
            } catch (UnsupportedOperationException exp) {
                Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
            }
        }

        @ParameterizedTest
        @DisplayName("should test neq")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldNEq(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();
                List<Fruit> result = template.typedQuery("FROM Fruit WHERE name <> :name", Fruit.class)
                        .bind("name", sample.getName())
                        .result();

                assertThat(result)
                        .isNotEmpty()
                        .allMatch(fruit -> !fruit.getName().equals(sample.getName()));
            } catch (UnsupportedOperationException exp) {
                Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
            }
        }

        @ParameterizedTest
        @DisplayName("should test gt")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldGt(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();
                List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity > :quantity", Fruit.class)
                        .bind("quantity", sample.getQuantity())
                        .result();

                assertThat(result)
                        .isNotEmpty()
                        .allMatch(fruit -> fruit.getQuantity() > sample.getQuantity());
            } catch (UnsupportedOperationException exp) {
                Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
            }
        }

        @ParameterizedTest
        @DisplayName("should test gte")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldGte(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();
                List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity >= :quantity", Fruit.class)
                        .bind("quantity", sample.getQuantity())
                        .result();

                assertThat(result)
                        .isNotEmpty()
                        .allMatch(fruit -> fruit.getQuantity() >= sample.getQuantity());
            } catch (UnsupportedOperationException exp) {
                Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
            }
        }

        @ParameterizedTest
        @DisplayName("should test lt")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldLt(List<Fruit> fruits) {
            try {
                template.insert(fruits);
                Fruit sample = fruits.getFirst();
                List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity < :quantity", Fruit.class)
                        .bind("quantity", sample.getQuantity())
                        .result();

                assertThat(result)
                        .isNotEmpty()
                        .allMatch(fruit -> fruit.getQuantity() < sample.getQuantity());
            } catch (UnsupportedOperationException exp) {
                Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
            }
        }

        @ParameterizedTest
        @DisplayName("should test lte")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldLte(List<Fruit> fruits) {
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity <= :quantity", Fruit.class)
                    .bind("quantity", sample.getQuantity())
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() <= sample.getQuantity());
        }

        @ParameterizedTest
        @DisplayName("should test in")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldIn(List<Fruit> fruits){
            template.insert(fruits);
            var sample1 = fruits.getFirst();
            var sample2 = fruits.get(1);
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE name IN (:name1, :name2)", Fruit.class)
                    .bind("name1", sample1.getName())
                    .bind("name2", sample2.getName())
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getName().equals(sample1.getName())
                            || fruit.getName().equals(sample2.getName()));
        }

    }

    @Nested
    @DisplayName("When there is no param binder")
    class WhenThereIsNoParamBinder {

        @ParameterizedTest
        @DisplayName("should test eq")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldEq(List<Fruit> fruits) {
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE name = '" + sample.getName() + "'", Fruit.class)
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getName().equals(sample.getName()));
        }

        @ParameterizedTest
        @DisplayName("should test neq")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldNEq(List<Fruit> fruits) {
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE name <> '" + sample.getName() + "'", Fruit.class)
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> !fruit.getName().equals(sample.getName()));
        }

        @ParameterizedTest
        @DisplayName("should test in")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldIn(List<Fruit> fruits){
            template.insert(fruits);
            var sample1 = fruits.getFirst();
            var sample2 = fruits.get(1);
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE name IN ('" + sample1.getName() + "', '" + sample2.getName() + "')", Fruit.class)
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getName().equals(sample1.getName())
                            || fruit.getName().equals(sample2.getName()));
        }

        @ParameterizedTest
        @DisplayName("should test gt")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldGt(List<Fruit> fruits){
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity > " + sample.getQuantity(), Fruit.class)
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() > sample.getQuantity());
        }

        @ParameterizedTest
        @DisplayName("should test gte")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldGte(List<Fruit> fruits){
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity >= " + sample.getQuantity(), Fruit.class)
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() >= sample.getQuantity());
        }

        @ParameterizedTest
        @DisplayName("should test lt")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldLt(List<Fruit> fruits) {
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity < " + sample.getQuantity(), Fruit.class)
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() < sample.getQuantity());
        }

        @ParameterizedTest
        @DisplayName("should test lte")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldLte(List<Fruit> fruits) {
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE quantity <= " + sample.getQuantity(), Fruit.class)
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() <= sample.getQuantity());
        }
    }
}
