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
import ee.jakarta.tck.nosql.entities.RecentSearches;
import ee.jakarta.tck.nosql.factories.PersonSupplier;
import ee.jakarta.tck.nosql.factories.RecentSearchesSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The basic template operations for POJO entities")
class BasicTemplateTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateTest.class.getName());

    @Nested
    @DisplayName("When inserting a POJO entity")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(PersonSupplier.class)
        @DisplayName("Should persist the POJO entity: {0}")
        void shouldInsert(Person entity) {

            // Given

            // When
            var person = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(person)
                        .as("inserted person")
                        .isNotNull();
                softly.assertThat(person.getId())
                        .as("inserted person id")
                        .isNotNull();
                softly.assertThat(person.getName())
                        .as("inserted person name")
                        .isEqualTo(entity.getName());
                softly.assertThat(person.getAge())
                        .as("inserted person age")
                        .isEqualTo(entity.getAge());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(PersonSupplier.class)
        @DisplayName("Should persist the POJO entity with TTL when supported: {0}")
        void shouldInsertWithTtl(Person person) {
            try {
                // Given

                // When
                var insertedPerson = template.insert(person, Duration.ofMinutes(10));

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(insertedPerson)
                            .as("inserted person with ttl")
                            .isNotNull();
                    softly.assertThat(insertedPerson.getId())
                            .as("inserted person with ttl id")
                            .isNotNull();
                    softly.assertThat(insertedPerson.getName())
                            .as("inserted person with ttl name")
                            .isEqualTo(person.getName());
                });
            } catch (UnsupportedOperationException exception) {
                LOGGER.info("TTL operation not supported by this database: " + exception.getMessage());
            }
        }

        @ParameterizedTest
        @ArgumentsSource(RecentSearchesSupplier.class)
        @DisplayName("Should persist the entity with a sequenced collection attribute")
        void shouldInsertSequencedCollectionEntity(RecentSearches entity) {

            // Given
            template.insert(entity);

            // When
            var recentSearches = template.find(RecentSearches.class, entity.getUserId());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(recentSearches)
                        .as("recent searches optional")
                        .isPresent();
                recentSearches.ifPresent(searches -> {
                    softly.assertThat(searches)
                            .as("recent searches entity")
                            .isNotNull();
                    softly.assertThat(searches.getUserId())
                            .as("recent searches user id")
                            .isNotNull();
                    softly.assertThat(searches.getKeywords())
                            .as("recent searches keywords")
                            .isNotNull()
                            .isNotEmpty();
                });
            });
        }
    }

    @Nested
    @DisplayName("When updating a POJO entity")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(PersonSupplier.class)
        @DisplayName("Should update the POJO entity: {0}")
        void shouldUpdate(Person entity) {

            // Given
            var insertedPerson = template.insert(entity);
            insertedPerson.setAge(insertedPerson.getAge() + 1);

            // When
            var updatedPerson = template.update(insertedPerson);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedPerson)
                        .as("updated person")
                        .isNotNull();
                softly.assertThat(updatedPerson.getId())
                        .as("updated person id")
                        .isEqualTo(insertedPerson.getId());
                softly.assertThat(updatedPerson.getAge())
                        .as("updated person age")
                        .isEqualTo(insertedPerson.getAge());
            });
        }
    }

    @Nested
    @DisplayName("When removing a POJO entity")
    class WhenTheRemoval {

        @ParameterizedTest
        @ArgumentsSource(PersonSupplier.class)
        @DisplayName("Should remove the POJO entity: {0}")
        void shouldDelete(Person entity) {

            // Given
            var insertedPerson = template.insert(entity);

            // When
            template.delete(Person.class, insertedPerson.getId());
            var deletedPerson = template.find(Person.class, insertedPerson.getId());

            // Then
            assertThat(deletedPerson)
                    .as("person after deletion")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("When searching for a POJO entity")
    class WhenTheSearch {

        @ParameterizedTest
        @ArgumentsSource(PersonSupplier.class)
        @DisplayName("Should return the POJO entity: {0}")
        void shouldFind(Person entity) {

            // Given
            var insertedPerson = template.insert(entity);

            // When
            var foundPerson = template.find(Person.class, insertedPerson.getId());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundPerson)
                        .as("found person optional")
                        .isPresent();
                foundPerson.ifPresent(person -> {
                    softly.assertThat(person.getId())
                            .as("found person id")
                            .isEqualTo(insertedPerson.getId());
                    softly.assertThat(person.getName())
                            .as("found person name")
                            .isEqualTo(insertedPerson.getName());
                    softly.assertThat(person.getAge())
                            .as("found person age")
                            .isEqualTo(insertedPerson.getAge());
                });
            });
        }
    }

    @Nested
    @DisplayName("When validating POJO entities")
    class WhenTheValidation {

        @Test
        @DisplayName("Should reject a null entity during insertion")
        void shouldRejectNullEntityOnInsert() {

            // Given

            // When / Then
            assertThatThrownBy(() -> template.insert(null))
                    .as("null entity insertion")
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should reject a null entity during update")
        void shouldRejectNullEntityOnUpdate() {

            // Given

            // When / Then
            assertThatThrownBy(() -> template.update((Person) null))
                    .as("null entity update")
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
