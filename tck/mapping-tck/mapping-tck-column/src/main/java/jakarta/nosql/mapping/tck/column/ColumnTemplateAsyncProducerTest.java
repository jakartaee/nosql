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
package jakarta.nosql.mapping.tck.column;

import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.mapping.column.ColumnTemplateAsyncProducer;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
public class ColumnTemplateAsyncProducerTest {


    @Inject
    private ColumnTemplateAsyncProducer producer;

    @Test
    public void shouldReturnErrorWhenColumnFamilyManagerNull() {
        Assertions.assertThrows(NullPointerException.class, () -> producer.get(null));
    }

    @Test
    public void shouldReturn() {
        ColumnFamilyManagerAsync manager = Mockito.mock(ColumnFamilyManagerAsync.class);
        ColumnTemplateAsync columnRepository = producer.get(manager);
        assertNotNull(columnRepository);
    }

}