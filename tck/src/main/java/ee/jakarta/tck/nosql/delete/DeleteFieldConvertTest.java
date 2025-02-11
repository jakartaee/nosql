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
package ee.jakarta.tck.nosql.delete;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Fruit;
import ee.jakarta.tck.nosql.factories.FruitListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

@DisplayName("Delete execution with @Convert annotated fields in the entity")
public class DeleteFieldConvertTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and delete with converted quantity equals condition")
    void shouldInsertIterableAndDeleteWithQuantityEqualsCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity();

            template.delete(Fruit.class)
                    .where("quantity")
                    .eq(targetQuantity)
                    .execute();

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .eq(targetQuantity)
                    .result();

            Assertions.assertThat(result).isEmpty();
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and delete with quantity greater-than condition")
    void shouldInsertIterableAndDeleteWithQuantityGreaterThanCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity() - 1;

            template.delete(Fruit.class)
                    .where("quantity")
                    .gt(targetQuantity)
                    .execute();

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .gt(targetQuantity)
                    .result();

            Assertions.assertThat(result).isEmpty();
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and delete with quantity less-than condition")
    void shouldInsertIterableAndDeleteWithQuantityLessThanCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity() + 10;

            template.delete(Fruit.class)
                    .where("quantity")
                    .lt(targetQuantity)
                    .execute();

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .lt(targetQuantity)
                    .result();

            Assertions.assertThat(result).isEmpty();
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(FruitListSupplier.class)
    @DisplayName("Should insert Iterable and delete with quantity between condition")
    void shouldInsertIterableAndDeleteWithQuantityBetweenCondition(List<Fruit> entities) {
        entities.forEach(template::insert);

        try {
            var targetQuantity = entities.get(0).getQuantity();

            template.delete(Fruit.class)
                    .where("quantity")
                    .between(targetQuantity - 5, targetQuantity + 5)
                    .execute();

            List<Fruit> result = template.select(Fruit.class)
                    .where("quantity")
                    .between(targetQuantity - 5, targetQuantity + 5)
                    .result();

            Assertions.assertThat(result).isEmpty();
        } catch (UnsupportedOperationException e) {
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
