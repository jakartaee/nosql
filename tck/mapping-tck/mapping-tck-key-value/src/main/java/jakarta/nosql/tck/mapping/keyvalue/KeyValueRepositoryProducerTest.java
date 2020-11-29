/*
 * Copyright (c) 2020 Otavio Santana and others
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.mapping.keyvalue;

import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.mapping.keyvalue.KeyValueRepositoryProducer;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.PersonRepository;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension(classes = {Person.class, PersonRepository.class})
class KeyValueRepositoryProducerTest {

    @Inject
    private KeyValueRepositoryProducer producer;

    @Test
    public void shouldCreateFromManager() {
        BucketManager manager = Mockito.mock(BucketManager.class);
        PersonRepository personRepository = producer.get(PersonRepository.class, manager);
        assertNotNull(personRepository);
    }

    @Test
    public void shouldCreateFromTemplate() {
        KeyValueTemplate template = Mockito.mock(KeyValueTemplate.class);
        PersonRepository personRepository = producer.get(PersonRepository.class, template);
        assertNotNull(personRepository);
    }

}