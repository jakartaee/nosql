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
import jakarta.nosql.tck.entities.Beer;
import jakarta.nosql.tck.entities.Coffee;
import jakarta.nosql.tck.entities.Drink;
import jakarta.nosql.tck.factories.DrinkListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;


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
                    .lt(entities.get(0).getAlcoholPercentage() + 1)
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
                    .sorted(Comparator.comparingDouble(Drink::getAlcoholPercentage))
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
                    .sorted(Comparator.comparingDouble(Drink::getAlcoholPercentage))
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

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("should select by type, where the type is a drink")
    void shouldSelectByType(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var coffees = template.select(Coffee.class).<Drink>result();
            var beers = template.select(Beer.class).<Drink>result();
            Assertions.assertThat(coffees).isNotEmpty().allMatch(Coffee.class::isInstance);
            Assertions.assertThat(beers).isNotEmpty().allMatch(Beer.class::isInstance);

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should select query using subtype")
    void shouldDoQueryBySubType(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var coffee = entities.stream().filter(Coffee.class::isInstance)
                    .map(Coffee.class::cast).findFirst().orElseThrow();
            var coffees = template.select(Coffee.class).where("country")
                    .eq(coffee.getCountry())
                    .<Coffee>result();

            Assertions.assertThat(coffees)
                    .isNotEmpty()
                    .allMatch(c -> c.getCountry().equals(coffee.getCountry()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should select query using subtype")
    void shouldDoQueryBySubType2(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var beer = entities.stream().filter(Beer.class::isInstance)
                    .map(Beer.class::cast).findFirst().orElseThrow();
            var beers = template.select(Beer.class).where("style")
                    .eq(beer.getStyle())
                    .<Beer>result();

            Assertions.assertThat(beers)
                    .isNotEmpty()
                    .allMatch(c -> c.getStyle().equals(beer.getStyle()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

}
