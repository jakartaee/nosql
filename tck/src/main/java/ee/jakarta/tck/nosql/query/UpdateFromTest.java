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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.Optional;

@DisplayName("The Jakarta Query integration test using update")
class UpdateFromTest extends AbstractTemplateTest {

    @ParameterizedTest
    @DisplayName("should update all entities")
    @ArgumentsSource(FruitListSupplier.class)
    void shouldUpdateEntities(List<Fruit> fruits) {
        try {
            template.insert(fruits);
            template.query("UPDATE Fruit SET quantity = 19").executeUpdate();
            List<Fruit> result = template.query("FROM Fruit").result();
            Assertions.assertThat(result).isNotEmpty().allMatch(fruit -> fruit.getQuantity() == 19);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should update all entities by params")
    @ArgumentsSource(FruitListSupplier.class)
    void shouldUpdateEntitiesByParams(List<Fruit> fruits) {
        try {
            template.insert(fruits);
            template.query("UPDATE Fruit SET quantity = :quantity")
                    .bind("quantity", 19)
                    .executeUpdate();
            List<Fruit> result = template.query("FROM Fruit").result();
            Assertions.assertThat(result).isNotEmpty().allMatch(fruit -> fruit.getQuantity() == 19);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should update entity by eq")
    @ArgumentsSource(FruitListSupplier.class)
    void shouldEq(List<Fruit> fruits) {
        try {
            template.insert(fruits);
            template.query("UPDATE Fruit SET quantity = :quantity WHERE id = :id")
                    .bind("quantity", 19)
                    .bind("id", fruits.getFirst().getId())
                    .executeUpdate();
            Optional<Fruit> result = template.query("FROM Fruit where id = :id")
                    .bind("id", fruits.getFirst().getId())
                    .singleResult();
            Assertions.assertThat(result)
                    .isNotEmpty()
                    .get()
                    .matches(fruit -> fruit.getQuantity() == 19);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should update entity by neq")
    @ArgumentsSource(FruitListSupplier.class)
    void shouldNEq(List<Fruit> fruits) {
        try {
            template.insert(fruits);
            template.query("UPDATE Fruit SET quantity = :quantity WHERE id <> :id")
                    .bind("quantity", 19)
                    .bind("id", fruits.getFirst().getId())
                    .executeUpdate();
            List<Fruit> result = template.query("FROM Fruit where id <> :id")
                    .bind("id", fruits.getFirst().getId())
                    .result();
            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() == 19);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should update entity by in")
    @ArgumentsSource(FruitListSupplier.class)
    void shouldIn(List<Fruit> fruits) {
        try {
            template.insert(fruits);
            template.query("UPDATE Fruit SET quantity = :quantity WHERE id IN (:ids)")
                    .bind("quantity", 19)
                    .bind("ids", fruits.stream().map(Fruit::getId).toList())
                    .executeUpdate();
            List<Fruit> result = template.query("FROM Fruit where id IN (:ids)")
                    .bind("ids", fruits.stream().map(Fruit::getId).toList())
                    .result();
            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() == 19);
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
            template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity > :quantity")
                    .bind("quantity", sample.getQuantity())
                    .executeUpdate();

            List<Fruit> result = template.query("FROM Fruit WHERE quantity > :quantity")
                    .bind("quantity", sample.getQuantity())
                    .result();
            Assertions.assertThat(result)
                    .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
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
            template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity >= :quantity")
                    .bind("quantity", sample.getQuantity())
                    .executeUpdate();

            List<Fruit> result = template.query("FROM Fruit WHERE quantity >= :quantity")
                    .bind("quantity", sample.getQuantity())
                    .result();
            Assertions.assertThat(result)
                    .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
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
            template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity < :quantity")
                    .bind("quantity", sample.getQuantity())
                    .executeUpdate();

            List<Fruit> result = template.query("FROM Fruit WHERE quantity < :quantity")
                    .bind("quantity", sample.getQuantity())
                    .result();
            Assertions.assertThat(result)
                    .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should test lte")
    @ArgumentsSource(FruitListSupplier.class)
    void shouldLte(List<Fruit> fruits) {
        try {
            template.insert(fruits);
            Fruit sample = fruits.getFirst();
            template.query("UPDATE Fruit SET name = 'Fruit Updated' WHERE quantity <= :quantity")
                    .bind("quantity", sample.getQuantity())
                    .executeUpdate();

            List<Fruit> result = template.query("FROM Fruit WHERE quantity <= :quantity")
                    .bind("quantity", sample.getQuantity())
                    .result();
            Assertions.assertThat(result)
                    .allMatch(fruit -> "Fruit Updated".equals(fruit.getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
