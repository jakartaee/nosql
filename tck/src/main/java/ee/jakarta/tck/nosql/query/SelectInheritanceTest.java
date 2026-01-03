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
import ee.jakarta.tck.nosql.entities.Drink;
import ee.jakarta.tck.nosql.factories.DrinkListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("The Jakarta Query integration test using select with inheritance")
class SelectInheritanceTest extends AbstractTemplateTest {

    //should select super mapped class
    //should select mapper class
    //should use specialization class

    @ParameterizedTest
    @ArgumentsSource(DrinkListSupplier.class)
    @DisplayName("Should select all entities from inherited hierarchy")
    void shouldSelectAllEntities(List<Drink> entities) {
        this.template.insert(entities);
        try {
            List<Drink> result = this.template.select(Drink.class).result();

            assertThat(result)
                    .isNotEmpty()
                    .hasSize(entities.size());
        } catch (UnsupportedOperationException exp) {
            assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

}
