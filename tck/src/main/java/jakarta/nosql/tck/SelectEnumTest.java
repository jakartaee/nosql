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


import jakarta.nosql.tck.entities.Vehicle;
import jakarta.nosql.tck.factories.VehicleListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

@DisplayName("The query execution exploring filters with enum")
public class SelectEnumTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(VehicleListSupplier.class)
    @DisplayName("Should insert Iterable and select with equals enum value")
    void shouldInsertIterableAndSelectWithEnumCondition(List<Vehicle> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Vehicle> result = template.select(Vehicle.class)
                    .where("transmission")
                    .eq(entities.get(0).getTransmission())
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(vehicle -> vehicle.getTransmission().equals(entities.get(0).getTransmission()));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(VehicleListSupplier.class)
    @DisplayName("Should insert Iterable and delete with equals enum value")
    void shouldInsertIterableAndDeleteWithEnumCondition(List<Vehicle> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Vehicle.class)
                    .where("transmission")
                    .eq(entities.get(0).getTransmission())
                    .execute();

            var result = template.select(Vehicle.class)
                    .where("transmission")
                    .eq(entities.get(0).getTransmission())
                    .result();

            Assertions.assertThat(result)
                    .isEmpty();
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
