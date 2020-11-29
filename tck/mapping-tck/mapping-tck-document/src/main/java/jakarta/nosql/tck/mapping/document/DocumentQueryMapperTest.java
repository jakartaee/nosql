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
package jakarta.nosql.tck.mapping.document;

import jakarta.nosql.mapping.document.DocumentQueryMapper;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperDeleteFrom;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CDIExtension
public class DocumentQueryMapperTest {

    @Inject
    private DocumentQueryMapper queryMapper;


    @Test
    public void shouldReturnErrorWhenEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> queryMapper.selectFrom(null));
    }

    @Test
    public void shouldReturnSelectFrom() {
        DocumentQueryMapper.DocumentMapperFrom documentFrom = queryMapper.selectFrom(Person.class);
        assertNotNull(documentFrom);
    }

    @Test
    public void shouldReturnErrorWhenDeleteEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> queryMapper.deleteFrom(null));
    }

    @Test
    public void shouldReturnDeleteFrom() {
        DocumentMapperDeleteFrom deleteFrom = queryMapper.deleteFrom(Person.class);
        assertNotNull(deleteFrom);
    }
}