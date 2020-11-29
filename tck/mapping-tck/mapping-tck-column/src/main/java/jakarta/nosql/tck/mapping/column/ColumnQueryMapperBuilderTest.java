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

import jakarta.nosql.mapping.column.ColumnQueryMapper;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperDeleteFrom;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperFrom;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CDIExtension
public class ColumnQueryMapperBuilderTest {

    @Inject
    private ColumnQueryMapper mapperBuilder;

    @Test
    public void shouldReturnErrorWhenEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> mapperBuilder.selectFrom(null));
    }

    @Test
    public void shouldReturnSelectFrom() {
        ColumnMapperFrom columnFrom = mapperBuilder.selectFrom(Person.class);
        assertNotNull(columnFrom);
    }

    @Test
    public void shouldReturnErrorWhenDeleteEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> mapperBuilder.deleteFrom(null));
    }

    @Test
    public void shouldReturnDeleteFrom() {
        ColumnMapperDeleteFrom columnDeleteFrom = mapperBuilder.deleteFrom(Person.class);
        assertNotNull(columnDeleteFrom);
    }
}