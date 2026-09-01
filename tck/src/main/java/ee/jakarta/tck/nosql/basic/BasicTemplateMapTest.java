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
import ee.jakarta.tck.nosql.entities.Computer;
import ee.jakarta.tck.nosql.entities.Contact;
import ee.jakarta.tck.nosql.entities.MobileSystem;
import ee.jakarta.tck.nosql.entities.Profile;
import ee.jakarta.tck.nosql.entities.Program;
import ee.jakarta.tck.nosql.factories.ComputerSupplier;
import ee.jakarta.tck.nosql.factories.ContactSupplier;
import ee.jakarta.tck.nosql.factories.MobileSystemSupplier;
import ee.jakarta.tck.nosql.factories.ProfileSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The basic template operations for entities with map attributes")
class BasicTemplateMapTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When inserting entities with map attributes")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(ContactSupplier.class)
        @DisplayName("Should persist the entity with a basic-value map: {0}")
        void shouldInsertBasicValueMap(Contact entity) {

            // Given

            // When
            var contact = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(contact)
                        .as("inserted contact")
                        .isNotNull();
                softly.assertThat(contact.getName())
                        .as("inserted contact name")
                        .isNotNull();
                softly.assertThat(contact.getSocialMedia())
                        .as("inserted contact social media")
                        .isEqualTo(entity.getSocialMedia());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(ProfileSupplier.class)
        @DisplayName("Should persist the record entity with a basic-value map: {0}")
        void shouldInsertRecordWithBasicValueMap(Profile entity) {

            // Given

            // When
            var profile = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(profile)
                        .as("inserted profile")
                        .isNotNull();
                softly.assertThat(profile.name())
                        .as("inserted profile name")
                        .isNotNull();
                softly.assertThat(profile.socialMedia())
                        .as("inserted profile social media")
                        .isEqualTo(entity.socialMedia());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(ComputerSupplier.class)
        @DisplayName("Should persist the entity with an embeddable-value map: {0}")
        void shouldInsertEmbeddableValueMap(Computer entity) {

            // Given

            // When
            var computer = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(computer)
                        .as("inserted computer")
                        .isNotNull();
                softly.assertThat(computer.getId())
                        .as("inserted computer id")
                        .isNotNull();
                softly.assertThat(computer.getPrograms())
                        .as("inserted computer programs")
                        .isEqualTo(entity.getPrograms());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(MobileSystemSupplier.class)
        @DisplayName("Should persist the record entity with an embeddable-value map: {0}")
        void shouldInsertRecordWithEmbeddableValueMap(MobileSystem entity) {

            // Given

            // When
            var mobileSystem = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(mobileSystem)
                        .as("inserted mobile system")
                        .isNotNull();
                softly.assertThat(mobileSystem.id())
                        .as("inserted mobile system id")
                        .isNotNull();
                softly.assertThat(mobileSystem.programs())
                        .as("inserted mobile system programs")
                        .isEqualTo(entity.programs());
            });
        }
    }

    @Nested
    @DisplayName("When updating entities with map attributes")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(ContactSupplier.class)
        @DisplayName("Should update the basic-value map: {0}")
        void shouldUpdateBasicValueMap(Contact entity) {

            // Given
            var insertedContact = template.insert(entity);
            insertedContact.put("socialMediaC", "https://new-social-media.com/profile");

            // When
            var updatedContact = template.update(insertedContact);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedContact)
                        .as("updated contact")
                        .isNotNull();
                softly.assertThat(updatedContact.getName())
                        .as("updated contact name")
                        .isEqualTo(insertedContact.getName());
                softly.assertThat(updatedContact.getSocialMedia())
                        .as("updated contact social media")
                        .isEqualTo(insertedContact.getSocialMedia());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(ProfileSupplier.class)
        @DisplayName("Should update the record basic-value map: {0}")
        void shouldUpdateRecordWithBasicValueMap(Profile entity) {

            // Given
            var insertedProfile = template.insert(entity);
            insertedProfile.put("socialMediaC", "https://new-social-media.com/profile");

            // When
            var updatedProfile = template.update(insertedProfile);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedProfile)
                        .as("updated profile")
                        .isNotNull();
                softly.assertThat(updatedProfile.name())
                        .as("updated profile name")
                        .isEqualTo(insertedProfile.name());
                softly.assertThat(updatedProfile.socialMedia())
                        .as("updated profile social media")
                        .isEqualTo(insertedProfile.socialMedia());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(ComputerSupplier.class)
        @DisplayName("Should update the embeddable-value map: {0}")
        void shouldUpdateEmbeddableValueMap(Computer entity) {

            // Given
            var insertedComputer = template.insert(entity);
            var program = Program.of("NewProgram", Map.of(
                    "infoA", "Some info A",
                    "infoB", "Some info B"
            ));
            insertedComputer.put("newProgram", program);

            // When
            var updatedComputer = template.update(insertedComputer);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedComputer)
                        .as("updated computer")
                        .isNotNull();
                softly.assertThat(updatedComputer.getId())
                        .as("updated computer id")
                        .isEqualTo(insertedComputer.getId());
                softly.assertThat(updatedComputer.getPrograms())
                        .as("updated computer programs")
                        .isEqualTo(insertedComputer.getPrograms());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(MobileSystemSupplier.class)
        @DisplayName("Should update the record embeddable-value map: {0}")
        void shouldUpdateRecordWithEmbeddableValueMap(MobileSystem entity) {

            // Given
            var insertedMobileSystem = template.insert(entity);
            var program = Program.of("NewProgram", Map.of(
                    "infoA", "Some info A",
                    "infoB", "Some info B"
            ));
            insertedMobileSystem.put("newProgram", program);

            // When
            var updatedMobileSystem = template.update(insertedMobileSystem);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedMobileSystem)
                        .as("updated mobile system")
                        .isNotNull();
                softly.assertThat(updatedMobileSystem.id())
                        .as("updated mobile system id")
                        .isEqualTo(insertedMobileSystem.id());
                softly.assertThat(updatedMobileSystem.programs())
                        .as("updated mobile system programs")
                        .isEqualTo(insertedMobileSystem.programs());
            });
        }
    }

    @Nested
    @DisplayName("When removing entities with map attributes")
    class WhenTheRemoval {

        @ParameterizedTest
        @ArgumentsSource(ContactSupplier.class)
        @DisplayName("Should remove the entity with a basic-value map: {0}")
        void shouldDeleteBasicValueMap(Contact entity) {

            // Given
            var insertedContact = template.insert(entity);

            // When
            template.delete(Contact.class, insertedContact.getName());
            var deletedContact = template.find(Contact.class, insertedContact.getName());

            // Then
            assertThat(deletedContact)
                    .as("contact after deletion")
                    .isEmpty();
        }

        @ParameterizedTest
        @ArgumentsSource(ProfileSupplier.class)
        @DisplayName("Should remove the record entity with a basic-value map: {0}")
        void shouldDeleteRecordWithBasicValueMap(Profile entity) {

            // Given
            var insertedProfile = template.insert(entity);

            // When
            template.delete(Profile.class, insertedProfile.name());
            var deletedProfile = template.find(Profile.class, insertedProfile.name());

            // Then
            assertThat(deletedProfile)
                    .as("profile after deletion")
                    .isEmpty();
        }

        @ParameterizedTest
        @ArgumentsSource(ComputerSupplier.class)
        @DisplayName("Should remove the entity with an embeddable-value map: {0}")
        void shouldDeleteEmbeddableValueMap(Computer entity) {

            // Given
            var insertedComputer = template.insert(entity);

            // When
            template.delete(Computer.class, insertedComputer.getId());
            var deletedComputer = template.find(Computer.class, insertedComputer.getId());

            // Then
            assertThat(deletedComputer)
                    .as("computer after deletion")
                    .isEmpty();
        }

        @ParameterizedTest
        @ArgumentsSource(MobileSystemSupplier.class)
        @DisplayName("Should remove the record entity with an embeddable-value map: {0}")
        void shouldDeleteRecordWithEmbeddableValueMap(MobileSystem entity) {

            // Given
            var insertedMobileSystem = template.insert(entity);

            // When
            template.delete(MobileSystem.class, insertedMobileSystem.id());
            var deletedMobileSystem = template.find(MobileSystem.class, insertedMobileSystem.id());

            // Then
            assertThat(deletedMobileSystem)
                    .as("mobile system after deletion")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("When searching for entities with map attributes")
    class WhenTheSearch {

        @ParameterizedTest
        @ArgumentsSource(ContactSupplier.class)
        @DisplayName("Should return the entity with a basic-value map: {0}")
        void shouldFindBasicValueMap(Contact entity) {

            // Given
            var insertedContact = template.insert(entity);

            // When
            var foundContact = template.find(Contact.class, insertedContact.getName());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundContact)
                        .as("found contact optional")
                        .isPresent();
                foundContact.ifPresent(contact -> softly.assertThat(contact.getName())
                        .as("found contact name")
                        .isEqualTo(insertedContact.getName()));
            });
        }

        @ParameterizedTest
        @ArgumentsSource(ProfileSupplier.class)
        @DisplayName("Should return the record entity with a basic-value map: {0}")
        void shouldFindRecordWithBasicValueMap(Profile entity) {

            // Given
            var insertedProfile = template.insert(entity);

            // When
            var foundProfile = template.find(Profile.class, insertedProfile.name());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundProfile)
                        .as("found profile optional")
                        .isPresent();
                foundProfile.ifPresent(profile -> softly.assertThat(profile.name())
                        .as("found profile name")
                        .isEqualTo(insertedProfile.name()));
            });
        }

        @ParameterizedTest
        @ArgumentsSource(ComputerSupplier.class)
        @DisplayName("Should return the entity with an embeddable-value map: {0}")
        void shouldFindEmbeddableValueMap(Computer entity) {

            // Given
            var insertedComputer = template.insert(entity);

            // When
            var foundComputer = template.find(Computer.class, insertedComputer.getId());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundComputer)
                        .as("found computer optional")
                        .isPresent();
                foundComputer.ifPresent(computer -> softly.assertThat(computer.getId())
                        .as("found computer id")
                        .isEqualTo(insertedComputer.getId()));
            });
        }

        @ParameterizedTest
        @ArgumentsSource(MobileSystemSupplier.class)
        @DisplayName("Should return the record entity with an embeddable-value map: {0}")
        void shouldFindRecordWithEmbeddableValueMap(MobileSystem entity) {

            // Given
            var insertedMobileSystem = template.insert(entity);

            // When
            var foundMobileSystem = template.find(MobileSystem.class, insertedMobileSystem.id());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundMobileSystem)
                        .as("found mobile system optional")
                        .isPresent();
                foundMobileSystem.ifPresent(mobileSystem -> softly.assertThat(mobileSystem.id())
                        .as("found mobile system id")
                        .isEqualTo(insertedMobileSystem.id()));
            });
        }
    }
}
