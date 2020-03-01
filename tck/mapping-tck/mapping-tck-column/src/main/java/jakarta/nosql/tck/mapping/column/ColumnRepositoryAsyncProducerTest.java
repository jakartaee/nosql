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
package jakarta.nosql.tck.mapping.column;

import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.mapping.column.ColumnRepositoryAsyncProducer;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.tck.entities.PersonRepositoryAsync;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
class ColumnRepositoryAsyncProducerTest {

    @Inject
    private ColumnRepositoryAsyncProducer producer;

    @Test
    public void shouldCreateFromManager() {
        ColumnFamilyManagerAsync manager= Mockito.mock(ColumnFamilyManagerAsync.class);
        PersonRepositoryAsync personRepository = producer.get(PersonRepositoryAsync.class, manager);
        assertNotNull(personRepository);
    }

    @Test
    public void shouldCreateFromTemplate() {
        ColumnTemplateAsync template= Mockito.mock(ColumnTemplateAsync.class);
        PersonRepositoryAsync personRepository = producer.get(PersonRepositoryAsync.class, template);
        assertNotNull(personRepository);
    }


}