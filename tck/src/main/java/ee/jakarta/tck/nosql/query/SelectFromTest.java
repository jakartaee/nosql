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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The Jakarta Query integration test using select without where clause")
class SelectFromTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When the select query is validated")
    class WhenTheSelectQueryIsValidated {

        @Test
        @DisplayName("Should reject null query definitions")
        void shouldRejectNullQueryDefinitions() {
            var queryFailure = catchThrowable(() -> template.query(null));
            var typedQueryFailure = catchThrowable(() -> template.typedQuery(null, null));

            assertSoftly(softly -> {
                softly.assertThat(queryFailure)
                        .as("failure for query(null)")
                        .isInstanceOf(NullPointerException.class);
                softly.assertThat(typedQueryFailure)
                        .as("failure for typedQuery(null, null)")
                        .isInstanceOf(NullPointerException.class);
            });
        }
    }

    @Nested
    @DisplayName("When unfiltered selection is executed")
    class WhenTheUnfilteredSelectionIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return all entities as a stream")
        void shouldReturnAllEntitiesAsAStream(List<Vehicle> vehicles) {
            try {
                template.insert(vehicles);

                var result = template.query("FROM Vehicle").stream().toList();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("selected entities from the stream query")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("entity count from the stream query")
                            .hasSize(vehicles.size());
                    softly.assertThat(result)
                            .as("entities returned from the stream query")
                            .containsAll(vehicles);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return all entities as a list")
        void shouldReturnAllEntitiesAsAList(List<Vehicle> vehicles) {
            try {
                template.insert(vehicles);

                var result = template.query("FROM Vehicle").result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("selected entities from the list query")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("entity count from the list query")
                            .hasSize(vehicles.size());
                    softly.assertThat(result)
                            .as("entities returned from the list query")
                            .containsAll(vehicles);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return all entities through a typed query")
        void shouldReturnAllEntitiesThroughATypedQuery(List<Vehicle> vehicles) {
            try {
                template.insert(vehicles);

                var result = template.typedQuery("", Vehicle.class).result();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("selected entities from the typed query")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("entity count from the typed query")
                            .hasSize(vehicles.size());
                    softly.assertThat(result)
                            .as("entities returned from the typed query")
                            .containsAll(vehicles);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When ordering by attribute is executed")
    class WhenTheOrderingByAttributeIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return entities in ascending attribute order")
        void shouldReturnEntitiesInAscendingAttributeOrder(List<Vehicle> vehicles) {
            try {
                template.insert(vehicles);

                List<Vehicle> result = template.query("FROM Vehicle ORDER BY color ASC").result();

                var expectedColors = vehicles.stream()
                        .map(Vehicle::getColor)
                        .sorted()
                        .toList();
                var returnedColors = result.stream()
                        .map(Vehicle::getColor)
                        .toList();

                assertSoftly(softly -> {
                    softly.assertThat(expectedColors)
                            .as("expected ascending attribute values")
                            .isNotEmpty();
                    softly.assertThat(expectedColors)
                            .as("expected ascending attribute value count")
                            .hasSize(vehicles.size());
                    softly.assertThat(expectedColors)
                            .as("returned ascending attribute values")
                            .containsExactly(returnedColors.toArray(new String[0]));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return entities in descending attribute order")
        void shouldReturnEntitiesInDescendingAttributeOrder(List<Vehicle> vehicles) {
            try {
                template.insert(vehicles);

                List<Vehicle> result = template.query("FROM Vehicle ORDER BY color DESC").result();

                var returnedColors = result.stream()
                        .map(Vehicle::getColor)
                        .toList();
                var expectedColors = vehicles.stream()
                        .map(Vehicle::getColor)
                        .sorted(Comparator.reverseOrder())
                        .toList();

                assertSoftly(softly -> {
                    softly.assertThat(expectedColors)
                            .as("expected descending attribute values")
                            .isNotEmpty();
                    softly.assertThat(expectedColors)
                            .as("expected descending attribute value count")
                            .hasSize(vehicles.size());
                    softly.assertThat(expectedColors)
                            .as("returned descending attribute values")
                            .containsExactly(returnedColors.toArray(new String[0]));
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When a projection is executed")
    class WhenTheProjectionIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return projected results")
        void shouldReturnProjectedResults(List<Vehicle> vehicles) {
            try {
                template.insert(vehicles);

                var result = template.typedQuery("FROM Vehicle", VehicleSummary.class).result();
                var expected = vehicles.stream()
                        .map(VehicleSummary::of)
                        .toList();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("projected results")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("projected result count")
                            .hasSize(vehicles.size());
                    softly.assertThat(result)
                            .as("expected projected results")
                            .containsAll(expected);
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    @Nested
    @DisplayName("When a count projection is executed")
    class WhenTheCountProjectionIsExecuted {

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should return the number of stored entities")
        void shouldReturnTheNumberOfStoredEntities(List<Vehicle> vehicles) {
            try {
                template.insert(vehicles);

                var result = template.query("SELECT count(this) FROM Vehicle").singleResult();

                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("entity count result")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("entity count value")
                            .contains((long) vehicles.size());
                });
            } catch (UnsupportedOperationException exception) {
                assertUnsupportedOperation(exception);
            }
        }
    }

    private void assertUnsupportedOperation(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("unsupported select query portability handling")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
