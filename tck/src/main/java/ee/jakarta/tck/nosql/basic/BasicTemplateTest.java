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
package ee.jakarta.tck.nosql.basic;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonSupplier;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

@DisplayName("The basic template operations using a POJO entity")
class BasicTemplateTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateTest.class.getName());

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should insert the person: {0}")
    void shouldInsert(Person entity) {
        var person = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(person).isNotNull();
            soft.assertThat(person.getId()).isNotNull();
            soft.assertThat(person.getName()).isEqualTo(entity.getName());
            soft.assertThat(person.getAge()).isEqualTo(entity.getAge());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should update the person: {0}")
    void shouldUpdate(Person entity) {
        var insertedPerson = template.insert(entity);

        insertedPerson.setAge(insertedPerson.getAge() + 1);
        var updatedPerson = template.update(insertedPerson);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedPerson).isNotNull();
            soft.assertThat(updatedPerson.getId()).isEqualTo(insertedPerson.getId());
            soft.assertThat(updatedPerson.getAge()).isEqualTo(insertedPerson.getAge());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should delete the person: {0}")
    void shouldDelete(Person entity) {
        var insertedPerson = template.insert(entity);

        template.delete(Person.class, insertedPerson.getId());

        var deletedPerson = template.find(Person.class, insertedPerson.getId());
        SoftAssertions.assertSoftly(soft -> soft.assertThat(deletedPerson).isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should find the person: {0}")
    void shouldFind(Person entity) {
        var insertedPerson = template.insert(entity);
        var foundPerson = template.find(Person.class, insertedPerson.getId());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(foundPerson).isPresent();
            soft.assertThat(foundPerson.orElseThrow().getId()).isEqualTo(insertedPerson.getId());
            soft.assertThat(foundPerson.orElseThrow().getName()).isEqualTo(insertedPerson.getName());
            soft.assertThat(foundPerson.orElseThrow().getAge()).isEqualTo(insertedPerson.getAge());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should insert vehicle with TTL")
    void shouldInsertWithTTL(Person person) {
        try {
            var insertedPerson = template.insert(person, Duration.ofMinutes(10));
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(insertedPerson).isNotNull();
                soft.assertThat(insertedPerson.getId()).isNotNull();
                soft.assertThat(insertedPerson.getName()).isEqualTo(person.getName());
            });
        } catch (UnsupportedOperationException e) {
            LOGGER.info("TTL operation not supported by this database: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should throw exception when null entity is inserted")
    void shouldThrowExceptionWhenNullEntityInserted() {
        Assertions.assertThatThrownBy(() -> template.insert(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw exception when null entity is updated")
    void shouldThrowExceptionWhenNullEntityUpdated() {
        Assertions.assertThatThrownBy(() -> template.update(null))
                .isInstanceOf(NullPointerException.class);
    }

}
