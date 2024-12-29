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
package jakarta.nosql.tck.select;

import jakarta.nosql.tck.AbstractTemplateTest;
import jakarta.nosql.tck.entities.Drink;
import jakarta.nosql.tck.factories.DrinkListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@DisplayName("The query execution exploring the inheritance annotation")
public class SelectTemplateInheritanceTest extends AbstractTemplateTest {


    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with less-than condition on alcoholPercentage")
    void shouldInsertIterableAndSelectWithLessThanCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .lt(entities.get(0).getAlcoholPercentage())
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getAlcoholPercentage() < entities.get(0).getAlcoholPercentage());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with less-than-or-equal condition on alcoholPercentage")
    void shouldInsertIterableAndSelectWithLessThanOrEqualCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .lte(entities.get(0).getAlcoholPercentage())
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getAlcoholPercentage() <= entities.get(0).getAlcoholPercentage());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with greater-than condition on alcoholPercentage")
    void shouldInsertIterableAndSelectWithGreaterThanCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var secondDrink = entities.stream()
                    .sorted((d1, d2) -> Double.compare(d1.getAlcoholPercentage(), d2.getAlcoholPercentage()))
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .gt(secondDrink.getAlcoholPercentage())
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getAlcoholPercentage() > secondDrink.getAlcoholPercentage());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with greater-than-or-equal condition on alcoholPercentage")
    void shouldInsertIterableAndSelectWithGreaterThanOrEqualCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .gte(entities.get(0).getAlcoholPercentage())
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getAlcoholPercentage() >= entities.get(0).getAlcoholPercentage());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'between' condition on alcoholPercentage")
    void shouldInsertIterableAndSelectWithBetweenCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var secondDrink = entities.stream()
                    .sorted((d1, d2) -> Double.compare(d1.getAlcoholPercentage(), d2.getAlcoholPercentage()))
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Drink.class)
                    .where("alcoholPercentage")
                    .between(secondDrink.getAlcoholPercentage(), secondDrink.getAlcoholPercentage() + 5)
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getAlcoholPercentage() >= secondDrink.getAlcoholPercentage()
                            && drink.getAlcoholPercentage() <= secondDrink.getAlcoholPercentage() + 5);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
