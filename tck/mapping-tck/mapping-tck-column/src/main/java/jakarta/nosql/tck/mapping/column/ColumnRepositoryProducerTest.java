/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package jakarta.nosql.tck.mapping.column;

import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.mapping.column.ColumnRepositoryProducer;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.tck.entities.PersonRepository;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
class ColumnRepositoryProducerTest {

    @Inject
    private ColumnRepositoryProducer producer;

    @Test
    public void shouldCreateFromManager() {
        ColumnFamilyManager manager= Mockito.mock(ColumnFamilyManager.class);
        PersonRepository personRepository = producer.get(PersonRepository.class, manager);
        assertNotNull(personRepository);
    }


    @Test
    public void shouldCreateFromTemplate() {
        ColumnTemplate template= Mockito.mock(ColumnTemplate.class);
        PersonRepository personRepository = producer.get(PersonRepository.class, template);
        assertNotNull(personRepository);
    }


}