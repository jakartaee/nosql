/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.nosql.factories;

import ee.jakarta.tck.nosql.entities.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PersonListSupplierTest {

    @Test
    @DisplayName("Should supply people with distinct ages")
    void shouldSupplyPeopleWithDistinctAges() {

        // Given
        var ages = List.of(10, 10, 20, 30, 40, 50, 60).iterator();
        var supplier = new PersonListSupplier() {
            @Override
            Person getEntity() {
                var person = new Person();
                person.setAge(ages.next());
                return person;
            }
        };

        // When
        var people = supplier.get();

        // Then
        assertThat(people)
                .extracting(Person::getAge)
                .containsExactly(10, 20, 30, 40, 50, 60);
    }
}
