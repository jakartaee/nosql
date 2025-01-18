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
package jakarta.nosql.tck.select;

import jakarta.nosql.tck.AbstractTemplateTest;
import jakarta.nosql.tck.entities.Fruit;
import jakarta.nosql.tck.factories.FruitListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

@DisplayName("Query execution with @Convert annotated fields in Fruit entity")
public class SelectFieldConvertTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and select with converted quantity equals condition")
    void shouldInsertIterableAndSelectWithQuantityEqualsCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity();

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .eq(targetQuantity)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity().equals(targetQuantity));
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and select with quantity greater-than condition")
    void shouldInsertIterableAndSelectWithQuantityGreaterThanCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity() - 1;

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .gt(targetQuantity)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() > targetQuantity);
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and select with quantity less-than condition")
    void shouldInsertIterableAndSelectWithQuantityLessThanCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity() + 10;

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .lt(targetQuantity)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getQuantity() < targetQuantity);
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and select with quantity between condition")
    void shouldInsertIterableAndSelectWithQuantityBetweenCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity();

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .between(targetQuantity - 5, targetQuantity + 5)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> {
                        var quantity = fruit.getQuantity();
                        return quantity >= (targetQuantity - 5) && quantity <= (targetQuantity + 5);
                    });
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
