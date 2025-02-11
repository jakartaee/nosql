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
package jakarta.nosql.tck.basic;

import jakarta.nosql.tck.AbstractTemplateTest;
import jakarta.nosql.tck.entities.Fruit;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.factories.FruitSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.util.logging.Logger;

@DisplayName("The basic template operations with entity that contains a converter field")
public class BasicConverterFieldTemplateTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicConverterFieldTemplateTest.class.getName());

    @ParameterizedTest
    @ArgumentsSource(FruitSupplier.class)
    @DisplayName("Should insert fruit with converted field: {0}")
    void shouldInsert(Fruit entity) {
        var fruit = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(fruit).isNotNull();
            soft.assertThat(fruit.getId()).isNotNull();
            soft.assertThat(fruit.getName()).isEqualTo(entity.getName());
            soft.assertThat(fruit.getPrice()).isEqualTo(entity.getPrice());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(FruitSupplier.class)
    @DisplayName("Should update fruit with converted field: {0}")
    void shouldUpdate(Fruit entity) {
        var insertedFruit = template.insert(entity);
        insertedFruit.setPrice(new Money(insertedFruit.getPrice().currency(), insertedFruit.getPrice().value().add(BigDecimal.TEN)));
        var updatedFruit = template.update(insertedFruit);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedFruit).isNotNull();
            soft.assertThat(updatedFruit.getPrice().value()).isEqualTo(insertedFruit.getPrice().value());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(FruitSupplier.class)
    @DisplayName("Should delete fruit with converted field: {0}")
    void shouldDelete(Fruit entity) {
        var insertedFruit = template.insert(entity);
        template.delete(Fruit.class, insertedFruit.getId());
        var deletedFruit = template.find(Fruit.class, insertedFruit.getId());
        SoftAssertions.assertSoftly(soft -> soft.assertThat(deletedFruit).isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(FruitSupplier.class)
    @DisplayName("Should find fruit with converted field: {0}")
    void shouldFind(Fruit entity) {
        var insertedFruit = template.insert(entity);
        var foundFruit = template.find(Fruit.class, insertedFruit.getId());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(foundFruit).isPresent();
            soft.assertThat(foundFruit.orElseThrow().getPrice()).isEqualTo(insertedFruit.getPrice());
        });
    }
}
