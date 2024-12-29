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
    @DisplayName("Should insert Iterable and select with no conditions")
    void shouldInsertIterableDrinkNoCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Drink> result = template.select(Drink.class)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .hasSize(entities.size());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with simple conditions")
    void shouldInsertIterableDrink(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Drink> result = template.select(Drink.class)
                    .where("name")
                    .eq(entities.get(0).getName())
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getName().equals(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with greater-than condition")
    void shouldInsertIterableAndSelectWithGreaterThanCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {

            var secondElder = entities.stream()
                    .mapToInt(drink -> drink.getName().length()) // Using length of name as a proxy for comparison
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Drink.class)
                    .where("name")
                    .gt(secondElder)
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getName().length() > secondElder);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with less-than condition")
    void shouldInsertIterableAndSelectWithLessThanCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {

            var secondElder = entities.stream()
                    .mapToInt(drink -> drink.getName().length()) // Using length of name as a proxy for comparison
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Drink.class)
                    .where("name")
                    .lt(secondElder)
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getName().length() < secondElder);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with LIKE condition")
    void shouldInsertIterableAndSelectWithLikeCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {
            List<Drink> result = template.select(Drink.class)
                    .where("name")
                    .like(entities.get(0).getName())
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getName().contains(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'in' condition")
    void shouldInsertIterableAndSelectWithInCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var result = template.select(Drink.class)
                    .where("name")
                    .in(List.of(entities.get(0).getName()))
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getName().equals(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'between' condition")
    void shouldInsertIterableAndSelectWithBetweenCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {
            var secondElder = entities.stream()
                    .mapToInt(drink -> drink.getName().length()) // Using length of name as a proxy for comparison
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Drink.class)
                    .where("name")
                    .between(secondElder, secondElder + 5)
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getName().length() >= secondElder && drink.getName().length() <= secondElder + 5);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'skip' and 'limit' conditions")
    void shouldInsertIterableAndSelectWithSkipAndLimitCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {

            var secondElder = entities.stream()
                    .mapToInt(drink -> drink.getName().length()) // Using length of name as a proxy for comparison
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Drink.class)
                    .where("name")
                    .gt(secondElder)
                    .skip(0)
                    .limit(10)
                    .<Drink>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(drink -> drink.getName().length() > secondElder);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'orderBy' condition")
    void shouldInsertIterableAndSelectWithOrderByCondition(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {

            var secondElder = entities.stream()
                    .mapToInt(drink -> drink.getName().length()) // Using length of name as a proxy for comparison
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Drink.class)
                    .where("name")
                    .gt(secondElder)
                    .orderBy("name")
                    .asc()
                    .<Drink>result();

            List<String> names = result.stream()
                    .map(Drink::getName)
                    .collect(Collectors.toList());

            Assertions.assertThat(names)
                    .isSorted();
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'complex' query using 'and'")
    void shouldInsertIterableAndSelectWithComplexQueryAnd(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {

            var secondElder = entities.stream()
                    .sorted(Comparator.comparing(drink -> drink.getName().length()))
                    .skip(entities.size() - 1)
                    .findFirst()
                    .orElseThrow();

            var length = secondElder.getName().length() - 1;
            var name = secondElder.getName();

            List<Drink> result = template.select(Drink.class)
                    .where("name")
                    .gt(length)
                    .and("name")
                    .eq(name)
                    .result();

            Assertions.assertThat(result).isNotEmpty()
                    .allMatch(drink -> drink.getName().length() > length
                            && drink.getName().equals(name));

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'complex' query using 'or'")
    void shouldInsertIterableAndSelectWithComplexQueryOr(List<Drink> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {

            var secondElder = entities.stream()
                    .sorted(Comparator.comparing(drink -> drink.getName().length()))
                    .skip(entities.size() - 1)
                    .findFirst()
                    .orElseThrow();

            List<Drink> result = template.select(Drink.class)
                    .where("name")
                    .gt(secondElder.getName().length())
                    .or("name")
                    .eq(secondElder.getName())
                    .result();

            Assertions.assertThat(result).isNotEmpty()
                    .allMatch(drink -> drink.getName().length() > secondElder.getName().length()
                            || drink.getName().equals(secondElder.getName()));

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
