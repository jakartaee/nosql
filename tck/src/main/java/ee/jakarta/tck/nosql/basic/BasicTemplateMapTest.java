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
import ee.jakarta.tck.nosql.entities.Contact;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.entities.Profile;
import ee.jakarta.tck.nosql.factories.ContactSupplier;
import ee.jakarta.tck.nosql.factories.ProfileSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

@DisplayName("The basic template operations using a POJO entity with a Map")
class BasicTemplateMapTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(ContactSupplier.class)
    @DisplayName("Should insert the contact: {0}")
    void shouldInsertMapWithBasicValueMap(Contact entity) {
        var contact = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(contact).isNotNull();
            soft.assertThat(contact.getName()).isNotNull();
            soft.assertThat(contact.getSocialMedia()).isEqualTo(entity.getSocialMedia());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(ContactSupplier.class)
    @DisplayName("Should update the contact: {0}")
    void shouldUpdateMapWithBasicValueMap(Contact entity) {
        var insertedContact = template.insert(entity);

        insertedContact.put("socialMediaC", "http://new-social-media.com/profile");
        var updatedPerson = template.update(insertedContact);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedPerson).isNotNull();
            soft.assertThat(updatedPerson.getName()).isEqualTo(insertedContact.getName());
            soft.assertThat(updatedPerson.getSocialMedia()).isEqualTo(insertedContact.getSocialMedia());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(ContactSupplier.class)
    @DisplayName("Should delete the contact: {0}")
    void shouldDeleteMapWithBasicValueMap(Contact entity) {
        var insert = template.insert(entity);

        template.delete(Contact.class, insert.getName());

        var deletedPerson = template.find(Contact.class, insert.getName());
        SoftAssertions.assertSoftly(soft -> soft.assertThat(deletedPerson).isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(ContactSupplier.class)
    @DisplayName("Should find the contact: {0}")
    void shouldFindMapWithBasicValueMap(Contact entity) {
        var inserted = template.insert(entity);
        var found = template.find(Contact.class, inserted.getName());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(found).isPresent();
            soft.assertThat(found.orElseThrow().getName()).isEqualTo(inserted.getName());
        });
    }

    //
    @ParameterizedTest
    @ArgumentsSource(ProfileSupplier.class)
    @DisplayName("Should insert the profile: {0}")
    void shouldInsertMapWithBasicValueMapOnRecord(Profile entity) {
        var contact = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(contact).isNotNull();
            soft.assertThat(contact.name()).isNotNull();
            soft.assertThat(contact.socialMedia()).isEqualTo(entity.socialMedia());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(ProfileSupplier.class)
    @DisplayName("Should update the profile: {0}")
    void shouldUpdateMapWithBasicValueMapOnRecord(Profile entity) {
        var insertedContact = template.insert(entity);

        insertedContact.put("socialMediaC", "http://new-social-media.com/profile");
        var updatedPerson = template.update(insertedContact);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedPerson).isNotNull();
            soft.assertThat(updatedPerson.name()).isEqualTo(insertedContact.name());
            soft.assertThat(updatedPerson.socialMedia()).isEqualTo(insertedContact.socialMedia());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(ProfileSupplier.class)
    @DisplayName("Should delete the profile: {0}")
    void shouldDeleteMapWithBasicValueMapOnRecord(Profile entity) {
        var insert = template.insert(entity);

        template.delete(Contact.class, insert.name());

        var deletedPerson = template.find(Contact.class, insert.name());
        SoftAssertions.assertSoftly(soft -> soft.assertThat(deletedPerson).isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(ProfileSupplier.class)
    @DisplayName("Should find the profile: {0}")
    void shouldFindMapWithBasicValueMapOnRecord(Profile entity) {
        var inserted = template.insert(entity);
        var found = template.find(Person.class, inserted.name());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(found).isPresent();
            soft.assertThat(found.orElseThrow().getName()).isEqualTo(inserted.name());
        });
    }


}
