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
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.entities.Vehicle;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import ee.jakarta.tck.nosql.factories.VehicleListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The template operations for iterable entities")
public class BasicIterableEntityTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When inserting iterable entities")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should persist an iterable of mutable entities")
        void shouldInsertMutableEntities(List<Person> entities) {

            // Given

            // When
            var result = StreamSupport.stream(template.insert(entities).spliterator(), false)
                    .toList();

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result)
                        .as("inserted people")
                        .hasSize(entities.size());
                result.forEach(person -> {
                    softly.assertThat(person)
                            .as("inserted person")
                            .isNotNull();
                    softly.assertThat(person.getId())
                            .as("inserted person id")
                            .isNotNull();
                    softly.assertThat(person.getName())
                            .as("inserted person name")
                            .isNotNull();
                    softly.assertThat(person.getAge())
                            .as("inserted person age")
                            .isNotNull();
                });
            });
        }

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should persist an iterable of entities with enum attributes")
        void shouldInsertEntitiesWithEnumAttribute(List<Vehicle> entities) {

            // Given

            // When
            var result = StreamSupport.stream(template.insert(entities).spliterator(), false)
                    .toList();

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result)
                        .as("inserted vehicles")
                        .hasSize(entities.size());
                result.forEach(vehicle -> {
                    softly.assertThat(vehicle)
                            .as("inserted vehicle")
                            .isNotNull();
                    softly.assertThat(vehicle.getId())
                            .as("inserted vehicle id")
                            .isNotNull();
                    softly.assertThat(vehicle.getModel())
                            .as("inserted vehicle model")
                            .isNotNull();
                    softly.assertThat(vehicle.getMake())
                            .as("inserted vehicle make")
                            .isNotNull();
                });
            });
        }
    }

    @Nested
    @DisplayName("When updating iterable entities")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should update an iterable of mutable entities")
        void shouldUpdateMutableEntities(List<Person> entities) {

            // Given
            var insertedPeople = StreamSupport.stream(template.insert(entities).spliterator(), false)
                    .toList();
            var updatedEntities = insertedPeople.stream()
                    .peek(person -> person.setName(person.getName() + "updated"))
                    .toList();

            // When
            var result = StreamSupport.stream(template.update(updatedEntities).spliterator(), false)
                    .toList();

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result)
                        .as("updated people")
                        .hasSize(entities.size());
                result.forEach(person -> {
                    softly.assertThat(person)
                            .as("updated person")
                            .isNotNull();
                    softly.assertThat(person.getId())
                            .as("updated person id")
                            .isNotNull();
                    softly.assertThat(person.getName())
                            .as("updated person name")
                            .isNotNull()
                            .contains("updated");
                    softly.assertThat(person.getAge())
                            .as("updated person age")
                            .isNotNull();
                });
            });
        }

        @ParameterizedTest
        @ArgumentsSource(VehicleListSupplier.class)
        @DisplayName("Should update an iterable of entities with enum attributes")
        void shouldUpdateEntitiesWithEnumAttribute(List<Vehicle> entities) {

            // Given
            var insertedVehicles = StreamSupport.stream(template.insert(entities).spliterator(), false)
                    .toList();
            var updatedEntities = insertedVehicles.stream()
                    .peek(vehicle -> vehicle.setModel(vehicle.getModel() + "updated"))
                    .toList();

            // When
            var result = StreamSupport.stream(template.update(updatedEntities).spliterator(), false)
                    .toList();

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result)
                        .as("updated vehicles")
                        .hasSize(entities.size());
                result.forEach(vehicle -> {
                    softly.assertThat(vehicle)
                            .as("updated vehicle")
                            .isNotNull();
                    softly.assertThat(vehicle.getId())
                            .as("updated vehicle id")
                            .isNotNull();
                    softly.assertThat(vehicle.getModel())
                            .as("updated vehicle model")
                            .isNotNull()
                            .contains("updated");
                    softly.assertThat(vehicle.getMake())
                            .as("updated vehicle make")
                            .isNotNull();
                });
            });
        }
    }
}
