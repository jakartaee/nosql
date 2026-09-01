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
import ee.jakarta.tck.nosql.entities.Transmission;
import ee.jakarta.tck.nosql.entities.Vehicle;
import ee.jakarta.tck.nosql.factories.VehicleSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The basic template operations for entities with enum attributes")
public class BasicEnumFieldTemplateTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicEnumFieldTemplateTest.class.getName());

    @Nested
    @DisplayName("When inserting an entity with an enum attribute")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(VehicleSupplier.class)
        @DisplayName("Should persist the entity with the enum attribute: {0}")
        void shouldInsert(Vehicle entity) {

            // Given

            // When
            var vehicle = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(vehicle)
                        .as("inserted vehicle")
                        .isNotNull();
                softly.assertThat(vehicle.getId())
                        .as("inserted vehicle id")
                        .isNotNull();
                softly.assertThat(vehicle.getModel())
                        .as("inserted vehicle model")
                        .isEqualTo(entity.getModel());
                softly.assertThat(vehicle.getTransmission())
                        .as("inserted vehicle transmission")
                        .isEqualTo(entity.getTransmission());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(VehicleSupplier.class)
        @DisplayName("Should persist the entity with TTL when supported: {0}")
        void shouldInsertWithTtl(Vehicle entity) {
            try {
                // Given

                // When
                var insertedVehicle = template.insert(entity, Duration.ofMinutes(10));

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(insertedVehicle)
                            .as("inserted vehicle with ttl")
                            .isNotNull();
                    softly.assertThat(insertedVehicle.getId())
                            .as("inserted vehicle with ttl id")
                            .isNotNull();
                    softly.assertThat(insertedVehicle.getModel())
                            .as("inserted vehicle with ttl model")
                            .isEqualTo(entity.getModel());
                });
            } catch (UnsupportedOperationException exception) {
                LOGGER.info("TTL operation not supported by this database: " + exception.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("When updating an entity with an enum attribute")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(VehicleSupplier.class)
        @DisplayName("Should update the transmission: {0}")
        void shouldUpdate(Vehicle entity) {

            // Given
            var insertedVehicle = template.insert(entity);
            insertedVehicle.setTransmission(Transmission.AUTOMATIC);

            // When
            var updatedVehicle = template.update(insertedVehicle);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedVehicle)
                        .as("updated vehicle")
                        .isNotNull();
                softly.assertThat(updatedVehicle.getTransmission())
                        .as("updated vehicle transmission")
                        .isEqualTo(Transmission.AUTOMATIC);
            });
        }
    }

    @Nested
    @DisplayName("When removing an entity with an enum attribute")
    class WhenTheRemoval {

        @ParameterizedTest
        @ArgumentsSource(VehicleSupplier.class)
        @DisplayName("Should remove the entity with the enum attribute: {0}")
        void shouldDelete(Vehicle entity) {

            // Given
            var insertedVehicle = template.insert(entity);

            // When
            template.delete(Vehicle.class, insertedVehicle.getId());
            var deletedVehicle = template.find(Vehicle.class, insertedVehicle.getId());

            // Then
            assertThat(deletedVehicle)
                    .as("vehicle after deletion")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("When searching for an entity with an enum attribute")
    class WhenTheSearch {

        @ParameterizedTest
        @ArgumentsSource(VehicleSupplier.class)
        @DisplayName("Should return the entity with the enum attribute: {0}")
        void shouldFind(Vehicle entity) {

            // Given
            var insertedVehicle = template.insert(entity);

            // When
            var foundVehicle = template.find(Vehicle.class, insertedVehicle.getId());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundVehicle)
                        .as("found vehicle optional")
                        .isPresent();
                foundVehicle.ifPresent(vehicle -> softly.assertThat(vehicle.getTransmission())
                        .as("found vehicle transmission")
                        .isEqualTo(insertedVehicle.getTransmission()));
            });
        }
    }
}
