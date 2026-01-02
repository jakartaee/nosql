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
package ee.jakarta.tck.nosql.query;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Vehicle;
import ee.jakarta.tck.nosql.entities.VehicleSummary;
import ee.jakarta.tck.nosql.factories.VehicleListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("The Jakarta Query integration test using select without where clause")
class SelectFromTest extends AbstractTemplateTest {

    @Test
    void shouldReturnErrorWhenQueryIsNull() {
        Assertions.assertThatThrownBy(() -> this.template.query(null))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> this.template.typedQuery(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("should find all entities as stream")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldFindAllEntities(List<Vehicle> vehicles) {
        template.insert(vehicles);
        var result = template.query("FROM Vehicle").stream();

        assertThat(result)
                .isNotEmpty()
                .hasSize(vehicles.size())
                .containsAll(vehicles);
    }

    @ParameterizedTest
    @DisplayName("should find all using class as list")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldFindAllUsingList(List<Vehicle> vehicles) {
        template.insert(vehicles);
        var result = template.query("FROM Vehicle").result();

        assertThat(result)
                .isNotEmpty()
                .hasSize(vehicles.size())
                .containsAll(vehicles);
    }

    @ParameterizedTest
    @DisplayName("should use typed class to select entities")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldUseTypedClass(List<Vehicle> vehicles) {
        template.insert(vehicles);
        var result = template.typedQuery("", Vehicle.class).result();
        assertThat(result)
                .isNotEmpty()
                .hasSize(vehicles.size())
                .containsAll(vehicles);
    }

    @ParameterizedTest
    @DisplayName("should order by ascending")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldOrderByAsc(List<Vehicle> vehicles) {
        template.insert(vehicles);
        var result = template.query("FROM Vehicle ORDER BY color ASC").result();
        assertThat(result)
                .isNotEmpty()
                .hasSize(vehicles.size())
                .containsExactly(vehicles.stream()
                        .sorted(comparing(Vehicle::getColor))
                        .toList());
    }

    @ParameterizedTest
    @DisplayName("should order by descending")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldOrderByDesc(List<Vehicle> vehicles) {
        template.insert(vehicles);
        var result = template.query("FROM Vehicle ORDER BY color DESC").result();
        assertThat(result)
                .isNotEmpty()
                .hasSize(vehicles.size())
                .containsExactly(vehicles.stream()
                        .sorted(comparing(Vehicle::getColor).reversed())
                        .toList());
    }

    @ParameterizedTest
    @DisplayName("should find all by projection")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldFindAllByProjection(List<Vehicle> vehicles) {
        template.insert(vehicles);
        var result = template.typedQuery("FROM Vehicle", VehicleSummary.class).result();

        var expected = vehicles.stream()
                .map(v -> new VehicleSummary(v.getId(), v.getModel(), v.getMake()))
                .toList();

        assertThat(result)
                .isNotEmpty()
                .hasSize(vehicles.size())
                .containsAll(expected);
    }

}
