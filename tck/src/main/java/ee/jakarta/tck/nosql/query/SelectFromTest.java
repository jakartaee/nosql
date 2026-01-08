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

import java.util.Comparator;
import java.util.List;

@DisplayName("The Jakarta Query integration test using select without where clause")
class SelectFromTest extends AbstractTemplateTest {

    @Test
    @DisplayName("should thrown NPE when parameter is null")
    void shouldThrownNPEWhenParameterIsNull() {
        Assertions.assertThatThrownBy(() -> this.template.query(null))
                .isInstanceOf(NullPointerException.class);
        Assertions.assertThatThrownBy(() -> this.template.typedQuery(null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @DisplayName("should find all entities as stream")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldFindAllEntities(List<Vehicle> vehicles) {
        try {
            template.insert(vehicles);
            var result = template.query("FROM Vehicle").stream();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .hasSize(vehicles.size())
                    .containsAll(vehicles);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should find all using class as list")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldFindAllUsingList(List<Vehicle> vehicles) {
        try {
            template.insert(vehicles);
            var result = template.query("FROM Vehicle").result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .hasSize(vehicles.size())
                    .containsAll(vehicles);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should use typed class to select entities")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldUseTypedClass(List<Vehicle> vehicles) {
        try {
            template.insert(vehicles);
            var result = template.typedQuery("", Vehicle.class).result();
            Assertions.assertThat(result)
                    .isNotEmpty()
                    .hasSize(vehicles.size())
                    .containsAll(vehicles);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should order by ascending")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldOrderByAsc(List<Vehicle> vehicles) {
        try {
            template.insert(vehicles);
            List<Vehicle> result = template.query("FROM Vehicle ORDER BY color ASC").result();

            var expectedColor = vehicles.stream()
                    .map(Vehicle::getColor)
                    .sorted()
                    .toList();

            var colors = result.stream()
                    .map(Vehicle::getColor)
                    .toList();

            Assertions.assertThat(expectedColor)
                    .isNotEmpty()
                    .hasSize(vehicles.size())
                    .containsExactly(colors.toArray(new String[0]));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should order by descending")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldOrderByDesc(List<Vehicle> vehicles) {
        try {
            template.insert(vehicles);
            List<Vehicle> result = template.query("FROM Vehicle ORDER BY color DESC").result();
            var colors = result.stream()
                    .map(Vehicle::getColor)
                    .toList();

            var expectedColor = vehicles.stream()
                    .map(Vehicle::getColor)
                    .sorted(Comparator.reverseOrder())
                    .toList();

            Assertions.assertThat(expectedColor)
                    .isNotEmpty()
                    .hasSize(vehicles.size())
                    .containsExactly(colors.toArray(new String[0]));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should find all by projection")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldFindAllByProjection(List<Vehicle> vehicles) {
        try {
            template.insert(vehicles);
            var result = template.typedQuery("FROM Vehicle", VehicleSummary.class).result();

            var expected = vehicles.stream()
                    .map(VehicleSummary::of)
                    .toList();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .hasSize(vehicles.size())
                    .containsAll(expected);
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @DisplayName("should count by")
    @ArgumentsSource(VehicleListSupplier.class)
    void shouldFindCount(List<Vehicle> vehicles) {
        try {
            template.insert(vehicles);
            var result = template.query("SELECT count(this) FROM Vehicle").singleResult();

            var expected = vehicles.stream()
                    .map(VehicleSummary::of)
                    .toList();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .isEqualTo(expected.size());
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
