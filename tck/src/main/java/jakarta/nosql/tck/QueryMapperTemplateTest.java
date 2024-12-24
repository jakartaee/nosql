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
package jakarta.nosql.tck;

import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.factories.PersonListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Collectors;


public class QueryMapperTemplateTest extends AbstractTemplateTest {

    @BeforeEach
    void cleanDatabase() {
        template.delete(Person.class).execute();
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with simple conditions")
    void shouldInsertIterablePerson(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Person> result = template.select(Person.class)
                    .where("name")
                    .eq(entities.get(0).getName())
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getName().equals(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with greater-than condition")
    void shouldInsertIterableAndSelectWithGreaterThanCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var result = template.select(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .<Person>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getAge() > entities.get(0).getAge());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with less-than condition")
    void shouldInsertIterableAndSelectWithLessThanCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {
            var result = template.select(Person.class)
                    .where("age")
                    .lt(entities.get(0).getAge())
                    .<Person>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getAge() < entities.get(0).getAge());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with LIKE condition")
    void shouldInsertIterableAndSelectWithLikeCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {

            List<Person> result = template.select(Person.class)
                    .where("name")
                    .like(entities.get(0).getName())
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getName().contains(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'in' condition")
    void shouldInsertIterableAndSelectWithInCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var result = template.select(Person.class)
                    .where("name")
                    .in(List.of(entities.get(0).getName()))
                    .<Person>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getName().equals(entities.get(0).getName()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'between' condition")
    void shouldInsertIterableAndSelectWithBetweenCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {
            var result = template.select(Person.class)
                    .where("age")
                    .between(entities.get(0).getAge(), entities.get(0).getAge() + 5)
                    .<Person>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getAge() >= entities.get(0).getAge() && person.getAge() <= entities.get(0).getAge() + 5);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'skip' and 'limit' conditions")
    void shouldInsertIterableAndSelectWithSkipAndLimitCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));
        try {

            var result = template.select(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .skip(0)
                    .limit(10)
                    .<Person>result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getAge() > entities.get(0).getAge());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'orderBy' condition")
    void shouldInsertIterableAndSelectWithOrderByCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {

            var result = template.select(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .orderBy("name")
                    .asc()
                    .<Person>result();

            List<String> names = result.stream()
                    .map(Person::getName)
                    .collect(Collectors.toList());

            Assertions.assertThat(names)
                    .isSorted();
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with 'complex' query")
    void shouldInsertIterableAndSelectWithComplexQuery(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Person> result = template.select(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .and("name")
                    .eq(entities.get(0).getName())
                    .result();

            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
