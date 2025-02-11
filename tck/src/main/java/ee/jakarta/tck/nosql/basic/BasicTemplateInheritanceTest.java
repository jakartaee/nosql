/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
import jakarta.nosql.tck.entities.Drink;
import jakarta.nosql.tck.factories.DrinkSupplier;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

@DisplayName("Basic operations exploring Inheritance with Inheritance annotations")
public class BasicTemplateInheritanceTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateInheritanceTest.class.getName());

    @ParameterizedTest
    @ArgumentsSource(DrinkSupplier.class)
    @DisplayName("Should insert the drink: {0}")
    void shouldInsert(Drink entity) {
        var drink = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(drink).isNotNull();
            soft.assertThat(drink.getId()).isNotNull();
            soft.assertThat(drink.getName()).isEqualTo(entity.getName());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkSupplier.class)
    @DisplayName("Should update the drink: {0}")
    void shouldUpdate(Drink entity) {
        var insertedDrink = template.insert(entity);

        insertedDrink.setName(insertedDrink.getName() + " Updated");
        var updatedDrink = template.update(insertedDrink);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedDrink).isNotNull();
            soft.assertThat(updatedDrink.getId()).isEqualTo(insertedDrink.getId());
            soft.assertThat(updatedDrink.getName()).isEqualTo(insertedDrink.getName());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkSupplier.class)
    @DisplayName("Should delete the drink: {0}")
    void shouldDelete(Drink entity) {
        var insertedDrink = template.insert(entity);

        template.delete(insertedDrink.getClass(), insertedDrink.getId());

        var deletedDrink = template.find(insertedDrink.getClass(), insertedDrink.getId());
        SoftAssertions.assertSoftly(soft -> soft.assertThat(deletedDrink).isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkSupplier.class)
    @DisplayName("Should find the drink: {0}")
    void shouldFind(Drink entity) {
        var insertedDrink = template.insert(entity);
        var foundDrink = template.find(insertedDrink.getClass(), insertedDrink.getId());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(foundDrink).isPresent();
            soft.assertThat(foundDrink.orElseThrow().getId()).isEqualTo(insertedDrink.getId());
            soft.assertThat(foundDrink.orElseThrow().getName()).isEqualTo(insertedDrink.getName());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkSupplier.class)
    @DisplayName("Should insert drink with TTL")
    void shouldInsertWithTTL(Drink drink) {
        try {
            var insertedDrink = template.insert(drink, Duration.ofMinutes(10));
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(insertedDrink).isNotNull();
                soft.assertThat(insertedDrink.getId()).isNotNull();
                soft.assertThat(insertedDrink.getName()).isEqualTo(drink.getName());
            });
        } catch (UnsupportedOperationException e) {
            LOGGER.info("TTL operation not supported by this database: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should throw exception when null entity is inserted")
    void shouldThrowExceptionWhenNullEntityInserted() {
        Assertions.assertThatThrownBy(() -> template.insert(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw exception when null entity is updated")
    void shouldThrowExceptionWhenNullEntityUpdated() {
        Assertions.assertThatThrownBy(() -> template.update(null))
                .isInstanceOf(NullPointerException.class);
    }
}
