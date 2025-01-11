/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.select;

import jakarta.nosql.tck.AbstractTemplateTest;
import jakarta.nosql.tck.entities.Animal;
import jakarta.nosql.tck.factories.AnimalListSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.logging.Logger;

@DisplayName("The query execution using MappedSuperclass annotation")
public class SelectMappedSuperclassTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(SelectMappedSuperclassTest.class.getName());

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with no conditions")
    void shouldSelectWithNoConditions(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        try {
            var result = template.select(Animal.class).result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).hasSize(animals.size());
            });
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with simple conditions")
    void shouldSelectWithSimpleConditions(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));
        try {
            var result = template.select(Animal.class)
                    .where("name")
                    .eq(animals.get(0).getName())
                    .<Animal>result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).allMatch(animal -> animal.getName().equals(animals.get(0).getName()));
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'greater-than' condition")
    void shouldSelectWithGreaterThanCondition(List<Animal> animals) {

        try {
            animals.forEach(animal -> template.insert(animal));


            var secondElder = animals.stream()
                    .mapToInt(Animal::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Animal.class)
                    .where("age")
                    .gt(secondElder)
                    .<Animal>result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).allMatch(animal -> animal.getAge() > secondElder);
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'less-than' condition")
    void shouldSelectWithLessThanCondition(List<Animal> animals) {

        try {
            animals.forEach(animal -> template.insert(animal));

            var secondElder = animals.stream()
                    .mapToInt(Animal::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Animal.class)
                    .where("age")
                    .lt(secondElder)
                    .<Animal>result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).allMatch(animal -> animal.getAge() < secondElder);
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with LIKE condition")
    void shouldSelectWithLikeCondition(List<Animal> animals) {

        try {

            animals.forEach(animal -> template.insert(animal));

            var result = template.select(Animal.class)
                    .where("name")
                    .like(animals.get(0).getName())
                    .<Animal>result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).allMatch(animal -> animal.getName().contains(animals.get(0).getName()));
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'in' condition")
    void shouldSelectWithInCondition(List<Animal> animals) {

        try {
            animals.forEach(animal -> template.insert(animal));

            var result = template.select(Animal.class)
                    .where("name")
                    .in(List.of(animals.get(0).getName()))
                    .<Animal>result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).allMatch(animal -> animal.getName().equals(animals.get(0).getName()));
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'between' condition")
    void shouldSelectWithBetweenCondition(List<Animal> animals) {

        try {

            animals.forEach(animal -> template.insert(animal));

            var age = animals.stream().map(Animal::getAge)
                    .sorted().findFirst()
                    .orElse(0);

            LOGGER.info("Min age: " + age);

            var result = template.select(Animal.class)
                    .where("age")
                    .between(age, age + 10)
                    .<Animal>result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).allMatch(animal -> animal.getAge() >= age && animal.getAge() <= age + 10);
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'skip' and 'limit' conditions")
    void shouldSelectWithSkipAndLimitCondition(List<Animal> animals) {

        try {

            animals.forEach(animal -> template.insert(animal));

            var secondOlder = animals.stream()
                    .mapToInt(Animal::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            var result = template.select(Animal.class)
                    .where("age")
                    .gt(secondOlder)
                    .skip(0)
                    .limit(10)
                    .<Animal>result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result).isNotEmpty();
                soft.assertThat(result).allMatch(animal -> animal.getAge() > secondOlder);
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }
}
