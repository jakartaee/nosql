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
package ee.jakarta.tck.nosql.select;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;

@DisplayName("Select Negate Query Tests")
public class SelectNegateTemplateTest extends AbstractTemplateTest {


    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated condition")
    void shouldInsertIterableAndSelectWithNegatedCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Person> result = template.select(Person.class)
                    .where("name")
                    .not().eq(entities.get(0).getName())
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> !person.getName().equals(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated greater-than condition")
    void shouldInsertIterableAndSelectWithNegatedGreaterThanCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var secondElder = entities.stream()
                    .mapToInt(Person::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .not().gt(secondElder)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getAge() <= secondElder);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated less-than condition")
    void shouldInsertIterableAndSelectWithNegatedLessThanCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var secondElder = entities.stream()
                    .mapToInt(Person::getAge)
                    .sorted()
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .not().lt(secondElder)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getAge() >= secondElder);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated LIKE condition")
    void shouldInsertIterableAndSelectWithNegatedLikeCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Person> result = template.select(Person.class)
                    .where("name")
                    .not().like(entities.get(0).getName())
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> !person.getName().contains(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated 'in' condition")
    void shouldInsertIterableAndSelectWithNegatedInCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Person> result = template.select(Person.class)
                    .where("name")
                    .not().in(List.of(entities.get(0).getName()))
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> !person.getName().equals(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated 'between' condition")
    void shouldInsertIterableAndSelectWithNegatedBetweenCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var secondElder = entities.stream()
                    .mapToInt(Person::getAge)
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .not().between(secondElder, secondElder + 5)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getAge() < secondElder || person.getAge() > secondElder + 5);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated complex query using 'and'")
    void shouldInsertIterableAndSelectWithNegatedComplexQueryAnd(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var secondElder = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge))
                    .skip(entities.size() - 1)
                    .findFirst()
                    .orElseThrow();

            var age = secondElder.getAge() -1;
            var name = secondElder.getName();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .not().gt(age)
                    .and("name")
                    .not().eq(name)
                    .result();

            Assertions.assertThat(result).isNotEmpty()
                    .allMatch(person -> person.getAge() <= age
                            && !person.getName().equals(name));

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with negated complex query using 'or'")
    void shouldInsertIterableAndSelectWithNegatedComplexQueryOr(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var secondElder = entities.stream()
                    .sorted(Comparator.comparing(Person::getAge))
                    .skip(entities.size() - 1)
                    .findFirst()
                    .orElseThrow();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .not().gt(secondElder.getAge())
                    .or("name")
                    .not().eq(secondElder.getName())
                    .result();

            Assertions.assertThat(result).isNotEmpty()
                    .allMatch(person -> person.getAge() <= secondElder.getAge()
                            || !person.getName().equals(secondElder.getName()));

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

}
