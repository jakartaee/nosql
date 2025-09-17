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
package ee.jakarta.tck.nosql.delete;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;

@DisplayName("The query execution delete with the basic operations on the fluent API")
public class DeleteBasicOperationsTemplateTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should execute basic operation with Equals")
    void shouldExecuteEq(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {

            String id = entities.get(0).getId();
            template.delete(Person.class)
                    .where("id").eq(id).execute();

            List<Person> result = template.select(Person.class)
                    .where("id").eq(id)
                    .result();

            Assertions.assertThat(result.stream()).isEmpty();
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

            template.delete(Person.class)
                    .where("age")
                    .gt(age)
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("age").gt(age)
                    .result();

            Assertions.assertThat(result).isEmpty();
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

            template.delete(Person.class)
                    .where("age").gte(age)
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("age").gte(age)
                    .result();

            Assertions.assertThat(result).isEmpty();
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

            template.delete(Person.class)
                    .where("age").lt(age)
                    .execute();
            List<Person> result = template.select(Person.class)
                    .where("age").lt(age)
                    .result();

            Assertions.assertThat(result).isEmpty();
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

            template.delete(Person.class).where("age").gte(age).execute();

            List<Person> result = template.select(Person.class)
                    .where("age").gte(age)
                    .result();

            Assertions.assertThat(result).isEmpty();
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

            template.delete(Person.class).where("id").in(ids).execute();

            List<Person> result = template.select(Person.class)
                    .where("id").in(ids)
                    .result();

            Assertions.assertThat(result).isEmpty();
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

            template.delete(Person.class).where("age").between(ageA, ageB).execute();

            List<Person> result = template.select(Person.class)
                    .where("age").between(ageA, ageB)
                    .result();

            Assertions.assertThat(result).isEmpty();
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
          var namePart =  entities.get(0).getName().substring(1, 3);

            template.delete(Person.class).where("name").contains(namePart).execute();

            List<Person> result = template.select(Person.class)
                    .where("name").contains(namePart)
                    .result();

            Assertions.assertThat(result).isEmpty();
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
            var namePart =  entities.get(0).getName().substring(1, 3);
            template.delete(Person.class)
                    .where("name").like("%" + namePart + "%")
                    .execute();
            List<Person> result = template.select(Person.class)
                    .where("name").like("%" + namePart + "%")
                    .result();

            Assertions.assertThat(result).isEmpty();
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
            var startsWith =  entities.get(0).getName().substring(0, 1);

            template.delete(Person.class)
                    .where("name").startsWith(startsWith)
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("name").startsWith(startsWith)
                    .result();

            Assertions.assertThat(result).isEmpty();
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
            var startsWith =  entities.get(0).getName().substring(0, 1);

            template.delete(Person.class).where("name").endsWith(startsWith).execute();

            List<Person> result = template.select(Person.class)
                    .where("name").endsWith(startsWith)
                    .result();

            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

}
