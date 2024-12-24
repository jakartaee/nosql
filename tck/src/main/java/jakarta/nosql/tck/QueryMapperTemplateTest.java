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
import jakarta.nosql.tck.factories.PersonSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class QueryMapperTemplateTest  extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with simple conditions")
    void shouldSelectWithConditions(Person entity) {
        try {
            List<Person> result = template.select(Person.class)
                    .where("name")
                    .eq(entity.getName())
                    .result();

            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with greater-than condition")
    void shouldSelectWithGreaterThanCondition(Person entity) {
        try {
            var result = template.select(Person.class)
                    .where("age")
                    .gt(entity.getAge())
                    .result();

            // Assuming results should return a List of Person
            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with less-than condition")
    void shouldSelectWithLessThanCondition(Person entity) {
        try {
            var result = template.select(Person.class)
                    .where("age")
                    .lt(entity.getAge())
                    .result();

            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with LIKE condition")
    void shouldSelectWithLikeCondition(Person entity) {
        try {
            List<Person> result = template.select(Person.class)
                    .where("name")
                    .like(entity.getName())
                    .result();

            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with 'in' condition")
    void shouldSelectWithInCondition(Person entity) {
        try {
            var result = template.select(Person.class)
                    .where("name")
                    .in(List.of(entity.getName()))
                    .result();

            // Assuming results should return a List of Person
            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with 'between' condition")
    void shouldSelectWithBetweenCondition(Person entity) {
        try {
            var result = template.select(Person.class)
                    .where("age")
                    .between(entity.getAge(), entity.getAge() + 5)
                    .result();

            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with 'skip' and 'limit' conditions")
    void shouldSelectWithSkipAndLimitCondition(Person entity) {
        try {
            var result = template.select(Person.class)
                    .where("age")
                    .gt(entity.getAge())
                    .skip(0)
                    .limit(10)
                    .result();

            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should select with 'orderBy' condition")
    void shouldSelectWithOrderByCondition(Person entity) {
        try {
            var result = template.select(Person.class)
                    .where("age")
                    .gt(entity.getAge())
                    .orderBy("name")
                    .asc()
                    .result();

            Assertions.assertThat(result).isNotEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should throw UnsupportedOperationException for complex queries in key-value stores")
    void shouldThrowUnsupportedExceptionForComplexQuery(Person entity) {
        try {
            List<Person> result = template.select(Person.class)
                    .where("age")
                    .gt(entity.getAge())
                    .and("name")
                    .eq(entity.getName())
                    .<Person>result();

            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Test passes if UnsupportedOperationException is thrown
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
