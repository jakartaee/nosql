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

import jakarta.nosql.tck.entities.Transmission;
import jakarta.nosql.tck.entities.Vehicle;
import jakarta.nosql.tck.factories.VehicleSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;


@DisplayName("The basic template operations with entity that contains enum")
public class BasicEnumFieldTemplateTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicEnumFieldTemplateTest.class.getName());

    @ParameterizedTest
    @ArgumentsSource(VehicleSupplier.class)
    @DisplayName("Should insert vehicle with enum: {0}")
    void shouldInsert(Vehicle entity) {
        var vehicle = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(vehicle).isNotNull();
            soft.assertThat(vehicle.getId()).isNotNull();
            soft.assertThat(vehicle.getModel()).isEqualTo(entity.getModel());
            soft.assertThat(vehicle.getTransmission()).isEqualTo(entity.getTransmission());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleSupplier.class)
    @DisplayName("Should update vehicle with enum: {0}")
    void shouldUpdate(Vehicle entity) {
        var insertedVehicle = template.insert(entity);
        insertedVehicle.setTransmission(Transmission.AUTOMATIC);
        var updatedVehicle = template.update(insertedVehicle);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedVehicle).isNotNull();
            soft.assertThat(updatedVehicle.getTransmission()).isEqualTo(Transmission.AUTOMATIC);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleSupplier.class)
    @DisplayName("Should delete vehicle with enum: {0}")
    void shouldDelete(Vehicle entity) {
        var insertedVehicle = template.insert(entity);
        template.delete(Vehicle.class, insertedVehicle.getId());
        var deletedVehicle = template.find(Vehicle.class, insertedVehicle.getId());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(deletedVehicle).isEmpty();
        });
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleSupplier.class)
    @DisplayName("Should find vehicle with enum: {0}")
    void shouldFind(Vehicle entity) {
        var insertedVehicle = template.insert(entity);
        var foundVehicle = template.find(Vehicle.class, insertedVehicle.getId());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(foundVehicle).isPresent();
            soft.assertThat(foundVehicle.orElseThrow().getTransmission()).isEqualTo(insertedVehicle.getTransmission());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleSupplier.class)
    @DisplayName("Should insert vehicle with TTL")
    void shouldInsertWithTTL(Vehicle entity) {
        try {
            Vehicle insertedVehicle = template.insert(entity, Duration.ofMinutes(10));
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(insertedVehicle).isNotNull();
                soft.assertThat(insertedVehicle.getId()).isNotNull();
                soft.assertThat(insertedVehicle.getModel()).isEqualTo(entity.getModel());
            });
        } catch (UnsupportedOperationException e) {
            LOGGER.info("TTL operation not supported by this database: " + e.getMessage());
        }
    }

}
