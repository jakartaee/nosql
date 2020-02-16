/*
 *  Copyright (c) 2020 Otavio Santana and others
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v. 2.0 which is available at
 *  http://www.eclipse.org/legal/epl-2.0.
 *
 *  This Source Code may also be made available under the following Secondary
 *  Licenses when the conditions for such availability set forth in the Eclipse
 *  Public License v. 2.0 are satisfied: GNU General Public License v2.0
 *  w/Classpath exception which is available at
 *  https://www.gnu.org/software/classpath/license.html.
 *
 *  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.communication.tck;


import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ColumnTest {

    private static final Value DEFAULT_VALUE = Value.of(12);

    @Test
    public void shouldReturnNameWhenNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Column column = Column.of(null, DEFAULT_VALUE);
        });
    }

    @Test
    public void shouldReturnNameWhenValueIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Column column = Column.of("Name", null);
        });
    }

    @Test
    public void shouldCreateAnColumnInstance() {
        String name = "name";
        Column column = Column.of(name, DEFAULT_VALUE);
        assertNotNull(column);
        assertEquals(name, column.getName());
        assertEquals(DEFAULT_VALUE, column.getValue());
    }

    @Test
    public void shouldBeEquals() {
        assertEquals(Column.of("name", DEFAULT_VALUE), Column.of("name", DEFAULT_VALUE));
    }

    @Test
    public void shouldReturnGetObject() {
        Value value = Value.of("text");
        Column column = Column.of("name", value);
        assertEquals(value.get(), column.get());
    }

    @Test
    public void shouldReturnGetClass() {
        Value value = Value.of("text");
        Column column = Column.of("name", value);
        assertEquals(value.get(String.class), column.get(String.class));
    }


    @Test
    public void shouldReturnGetType() {
        Value value = Value.of("text");
        Column column = Column.of("name", value);
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>(){};
        assertEquals(value.get(typeReference), column.get(typeReference));
    }
}
