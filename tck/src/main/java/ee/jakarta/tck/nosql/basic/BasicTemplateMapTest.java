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
import ee.jakarta.tck.nosql.factories.ContactSupplier;
import ee.jakarta.tck.nosql.factories.PersonSupplier;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

@DisplayName("The basic template operations using a POJO entity with a Map")
class BasicTemplateMapTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateMapTest.class.getName());

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
    void shouldFind(Contact entity) {
        var inserted = template.insert(entity);
        var found = template.find(Person.class, inserted.getName());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(found).isPresent();
            soft.assertThat(found.orElseThrow().getName()).isEqualTo(inserted.getName());
        });
    }


}
