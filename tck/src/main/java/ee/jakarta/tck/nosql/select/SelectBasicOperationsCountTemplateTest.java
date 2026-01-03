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

@DisplayName("The query execution select with the basic operations on the fluent API with count")
public class SelectBasicOperationsCountTemplateTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with Equals")
    void shouldExecuteEq(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            String id = entities.getFirst().getId();
            var count = template.select(Person.class)
                    .where("id").eq(id)
                    .count();

            var expected = entities.stream().filter(person -> person.getId().equals(id)).count();
            Assertions.assertThat(expected).isEqualTo(count);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with GT")
    void shouldExecuteGt(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var age = entities.stream().sorted(Comparator.comparing(Person::getAge)).skip(1).findFirst().orElseThrow().getAge();
            var count = template.select(Person.class)
                    .where("age").gt(age)
                    .count();
            var expected = entities.stream().filter(person -> person.getAge() > age).count();
            Assertions.assertThat(count).isEqualTo(expected);

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with GTE")
    void shouldExecuteGte(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var age = entities.stream().sorted(Comparator.comparing(Person::getAge)).skip(1).findFirst().orElseThrow().getAge();
            var count = template.select(Person.class)
                    .where("age").gte(age)
                    .count();
            var expected = entities.stream().filter(person -> person.getAge() >= age).count();
            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with LT")
    void shouldExecuteLt(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var age = entities.stream().sorted(Comparator.comparing(Person::getAge).reversed()).skip(1).findFirst().orElseThrow().getAge();
            var count = template.select(Person.class)
                    .where("age").lt(age)
                    .count();

            var expected = entities.stream().filter(person -> person.getAge() < age).count();

            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with LTE")
    void shouldExecuteLte(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var age = entities.stream().sorted(Comparator.comparing(Person::getAge).reversed()).skip(1).findFirst().orElseThrow().getAge();
            var count = template.select(Person.class)
                    .where("age").lte(age)
                    .count();
            var expected = entities.stream().filter(person -> person.getAge() <= age).count();

            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with In")
    void shouldExecuteIn(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var ids = entities.stream().map(Person::getId).limit(3).toList();
            var count = template.select(Person.class)
                    .where("id").in(ids)
                    .count();

            var expected = entities.stream().filter(person -> ids.contains(person.getId())).count();
            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with between")
    void shouldExecuteBetween(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var ageA = entities.stream().sorted(Comparator.comparing(Person::getAge)).skip(1).findFirst().orElseThrow().getAge();
            var ageB = entities.stream().sorted(Comparator.comparing(Person::getAge)).skip(3).findFirst().orElseThrow().getAge();
            var count = template.select(Person.class)
                    .where("age").between(ageA, ageB)
                    .count();

            var expected = entities.stream().filter(person -> person.getAge() <= ageB && person.getAge() >= ageA).count();

            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with contains")
    void shouldExecuteContains(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
          var namePart =  entities.getFirst().getName().substring(1, 3);
            var count = template.select(Person.class)
                    .where("name").contains(namePart)
                    .count();

            var expected = entities.stream().filter(person -> person.getName().contains(namePart)).count();

            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with Like")
    void shouldExecuteLike(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var namePart =  entities.getFirst().getName().substring(1, 3);
            var count = template.select(Person.class)
                    .where("name").like("%" + namePart + "%")
                    .count();

            var expected = entities.stream().filter(person -> person.getName().contains(namePart)).count();
            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with startsWith")
    void shouldExecuteStartsWith(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var startsWith =  entities.getFirst().getName().substring(0, 1);
            var count = template.select(Person.class)
                    .where("name").startsWith(startsWith)
                    .count();

            var expected = entities.stream().filter(person -> person.getName().startsWith(startsWith)).count();

            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with startsWith")
    void shouldExecuteEndsWith(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            var startsWith =  entities.getFirst().getName().substring(0, 1);
            var count = template.select(Person.class)
                    .where("name").endsWith(startsWith)
                    .count();

            var expected = entities.stream().filter(person -> person.getName().endsWith(startsWith)).count();
            Assertions.assertThat(count).isEqualTo(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

}
