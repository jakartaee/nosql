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
package ee.jakarta.tck.nosql.delete;


import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Beer;
import ee.jakarta.tck.nosql.entities.Drink;
import ee.jakarta.tck.nosql.factories.DrinkListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

public class DeleteTemplateInheritanceTest extends AbstractTemplateTest {


    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with no conditions")
    void shouldInsertIterableAndDeleteNoCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Drink.class).execute();

            List<Drink> result = template.select(Drink.class).result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with simple condition on alcoholPercentage")
    void shouldInsertIterableAndDeleteWithSimpleCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Drink.class)
                    .where("alcoholPercentage")
                    .eq(entities.get(0).getAlcoholPercentage())
                    .execute();

            List<Drink> result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .eq(entities.get(0).getAlcoholPercentage())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with greater-than condition on alcoholPercentage")
    void shouldInsertIterableAndDeleteWithGreaterThanCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Drink.class)
                    .where("alcoholPercentage")
                    .gt(entities.get(0).getAlcoholPercentage())
                    .execute();

            List<Drink> result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .gt(entities.get(0).getAlcoholPercentage())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with less-than condition on alcoholPercentage")
    void shouldInsertIterableAndDeleteWithLessThanCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Drink.class)
                    .where("alcoholPercentage")
                    .lt(entities.get(0).getAlcoholPercentage())
                    .execute();

            List<Drink> result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .lt(entities.get(0).getAlcoholPercentage())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'in' condition on alcoholPercentage")
    void shouldInsertIterableAndDeleteWithInCondition(List<Drink> entities) {
        // Insert the entities
        entities.forEach(entity -> template.insert(entity));

        try {
            // Delete based on the 'alcoholPercentage' field (in a list of values)
            template.delete(Drink.class)
                    .where("alcoholPercentage")
                    .in(List.of(entities.get(0).getAlcoholPercentage()))
                    .execute();

            // Verify that no drinks with the given alcoholPercentage exist
            List<Drink> result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .in(List.of(entities.get(0).getAlcoholPercentage()))
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'between' condition on alcoholPercentage")
    void shouldInsertIterableAndDeleteWithBetweenCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Drink.class)
                    .where("alcoholPercentage")
                    .between(entities.get(0).getAlcoholPercentage(), entities.get(0).getAlcoholPercentage() + 5)
                    .execute();

            List<Drink> result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .between(entities.get(0).getAlcoholPercentage(), entities.get(0).getAlcoholPercentage() + 5)
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'complex' query using 'and' on alcoholPercentage")
    void shouldInsertIterableAndDeleteWithComplexQueryAnd(List<Drink> entities) {
        // Insert the entities
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Drink.class)
                    .where("alcoholPercentage")
                    .gt(entities.get(0).getAlcoholPercentage())
                    .and("name")
                    .eq(entities.get(0).getName())
                    .execute();

            List<Drink> result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .gt(entities.get(0).getAlcoholPercentage())
                    .and("name")
                    .eq(entities.get(0).getName())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }


    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and delete with no conditions")
    void shouldDeleteUsingSubType(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Beer.class).execute();

            List<Drink> result = template.select(Drink.class).result();
            Assertions.assertThat(result).isNotEmpty().allMatch(d -> !d.getClass().equals(Beer.class));

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
