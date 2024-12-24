/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck;

import jakarta.nosql.tck.entities.Animal;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.factories.AnimalListSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Collectors;

public class QueryTemplateInheritanceTest extends AbstractTemplateTest{

    @BeforeEach
    void cleanDatabase() {
        template.delete(Person.class).execute();
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with no conditions")
    void shouldSelectWithNoConditions(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class).result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).hasSize(animals.size());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with simple conditions")
    void shouldSelectWithSimpleConditions(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("name")
                .eq(animals.get(0).getName())
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).allMatch(animal -> animal.getName().equals(animals.get(0).getName()));
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'greater-than' condition")
    void shouldSelectWithGreaterThanCondition(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("species")
                .gt(animals.get(0).getSpecies())
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).allMatch(animal -> animal.getSpecies().compareTo(animals.get(0).getSpecies()) > 0);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'less-than' condition")
    void shouldSelectWithLessThanCondition(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("species")
                .lt(animals.get(0).getSpecies())
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).allMatch(animal -> animal.getSpecies().compareTo(animals.get(0).getSpecies()) < 0);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with LIKE condition")
    void shouldSelectWithLikeCondition(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("name")
                .like(animals.get(0).getName())
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).allMatch(animal -> animal.getName().contains(animals.get(0).getName()));
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'in' condition")
    void shouldSelectWithInCondition(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("name")
                .in(List.of(animals.get(0).getName()))
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).allMatch(animal -> animal.getName().equals(animals.get(0).getName()));
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'between' condition")
    void shouldSelectWithBetweenCondition(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("species")
                .between(animals.get(0).getSpecies(), "Zebra")
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).allMatch(animal -> animal.getSpecies().compareTo(animals.get(0).getSpecies()) >= 0 && animal.getSpecies().compareTo("Zebra") <= 0);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'skip' and 'limit' conditions")
    void shouldSelectWithSkipAndLimitCondition(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("species")
                .gt(animals.get(0).getSpecies())
                .skip(0)
                .limit(10)
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result).allMatch(animal -> animal.getSpecies().compareTo(animals.get(0).getSpecies()) > 0);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should select animals with 'orderBy' condition")
    void shouldSelectWithOrderByCondition(List<Animal> animals) {
        animals.forEach(animal -> template.insert(animal));

        var result = template.select(Animal.class)
                .where("species")
                .gt(animals.get(0).getSpecies())
                .orderBy("name")
                .asc()
                .<Animal>result();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isNotEmpty();
            soft.assertThat(result.stream()
                            .map(Animal::getName)
                            .collect(Collectors.toList()))
                    .isSorted();
        });
    }
}
