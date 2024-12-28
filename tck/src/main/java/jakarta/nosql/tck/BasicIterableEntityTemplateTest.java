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
import jakarta.nosql.tck.entities.Vehicle;
import jakarta.nosql.tck.factories.PersonListSupplier;
import jakarta.nosql.tck.factories.VehicleListSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

@DisplayName("The iterable template operations")
public class BasicIterableEntityTemplateTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert a list of persons")
    void shouldInsertIterablePerson(List<Person> entities) {
        Iterable<Person> result = template.insert(entities);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).hasSize(entities.size());
            result.forEach(person -> {
                soft.assertThat(person).isNotNull();
                soft.assertThat(person.getId()).isNotNull();
                soft.assertThat(person.getName()).isNotNull();
                soft.assertThat(person.getAge()).isNotNull();
            });
        });
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleListSupplier.class)
    @DisplayName("Should insert a list of vehicles")
    void shouldInsertIterableVehicle(List<Vehicle> entities) {
        Iterable<Vehicle> result = template.insert(entities);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).hasSize(entities.size());
            result.forEach(vehicle -> {
                soft.assertThat(vehicle).isNotNull();
                soft.assertThat(vehicle.getId()).isNotNull();
                soft.assertThat(vehicle.getModel()).isNotNull();
                soft.assertThat(vehicle.getMake()).isNotNull();
            });
        });
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should update a list of persons")
    void shouldUpdateIterablePerson(List<Person> entities) {
        Iterable<Person> result = template.update(entities);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).hasSize(entities.size());
            result.forEach(person -> {
                soft.assertThat(person).isNotNull();
                soft.assertThat(person.getId()).isNotNull();
                soft.assertThat(person.getName()).isNotNull();
                soft.assertThat(person.getAge()).isNotNull();
            });
        });
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleListSupplier.class)
    @DisplayName("Should update a list of vehicles")
    void shouldUpdateIterableVehicle(List<Vehicle> entities) {
        Iterable<Vehicle> result = template.update(entities);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).hasSize(entities.size());
            result.forEach(vehicle -> {
                soft.assertThat(vehicle).isNotNull();
                soft.assertThat(vehicle.getId()).isNotNull();
                soft.assertThat(vehicle.getModel()).isNotNull();
                soft.assertThat(vehicle.getMake()).isNotNull();
            });
        });
    }
}
