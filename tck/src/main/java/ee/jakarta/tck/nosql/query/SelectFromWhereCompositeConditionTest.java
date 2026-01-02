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
import ee.jakarta.tck.nosql.entities.Fruit;
import ee.jakarta.tck.nosql.factories.FruitListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("The Jakarta Query integration test using select with where composite condition (AND, OR)")
public class SelectFromWhereCompositeConditionTest extends AbstractTemplateTest {


    @Nested
    @DisplayName("When there is param binder")
    class WhenThereIsParamBinder {

        @ParameterizedTest
        @DisplayName("should test and")
        @ArgumentsSource(FruitListSupplier.class)
        void shouldAnd(List<Fruit> fruits) {
            template.insert(fruits);
            Fruit sample = fruits.get(0);
            List<Fruit> result = template.typedQuery("FROM Fruit WHERE name = :name AND quantity = :quantity", Fruit.class)
                    .bind("name", sample.getName())
                    .bind("quantity", sample.getQuantity())
                    .result();

            assertThat(result)
                    .isNotEmpty()
                    .allMatch(fruit -> fruit.getName().equals(sample.getName())
                            && fruit.getQuantity().equals(sample.getQuantity()));
        }

    }

    @Nested
    @DisplayName("When there is no param binder")
    class WhenThereIsNoParamBinder {

    }

    //should have query with AND
    //should have query with OR
    //should have query with AND & OR combined
}
