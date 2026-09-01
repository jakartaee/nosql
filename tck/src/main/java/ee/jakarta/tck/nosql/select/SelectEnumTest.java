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
import ee.jakarta.tck.nosql.entities.Vehicle;
import ee.jakarta.tck.nosql.factories.VehicleListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The query execution exploring enum attribute filters")
public class SelectEnumTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting entities by enum attribute equality")
    class WhenTheEnumAttributeEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return only entities with the requested enum attribute")
        void shouldReturnOnlyMatchingEntities(List<Vehicle> entities) {

            // Given
            insertVehicles(entities);
            var transmission = entities.getFirst().getTransmission();

            try {
                // When
                List<Vehicle> result = template.select(Vehicle.class)
                        .where("transmission")
                        .eq(transmission)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("vehicles returned for the transmission filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("vehicles matching the requested transmission")
                            .allMatch(vehicle -> vehicle.getTransmission().equals(transmission));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When deleting entities by enum attribute equality")
    class WhenTheEnumAttributeEqualityDeletion {

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should remove every entity with the requested enum attribute")
        void shouldRemoveMatchingEntities(List<Vehicle> entities) {

            // Given
            insertVehicles(entities);
            var transmission = entities.getFirst().getTransmission();

            try {
                // When
                template.delete(Vehicle.class)
                        .where("transmission")
                        .eq(transmission)
                        .execute();

                List<Vehicle> result = template.select(Vehicle.class)
                        .where("transmission")
                        .eq(transmission)
                        .result();

                // Then
                assertThat(result)
                        .as("vehicles remaining after deleting by transmission")
                        .isEmpty();
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    private void insertVehicles(List<Vehicle> entities) {
        entities.forEach(template::insert);
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported enum-based select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
