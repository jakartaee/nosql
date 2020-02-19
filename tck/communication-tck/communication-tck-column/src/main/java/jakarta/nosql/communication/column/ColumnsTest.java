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

package jakarta.nosql.communication.column;

import jakarta.nosql.column.Column;
import jakarta.nosql.column.Columns;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class ColumnsTest {

    @Test
    public void shouldCreateColumn() {
        Column column = Columns.of("name", "Ada");
        assertEquals("name", column.getName());
        assertEquals("Ada", column.get());
    }

    @Test
    public void shouldCreateColumnsFromMap() {
        Map<String, String> map = singletonMap("name", "Ada");
        List<Column> columns = Columns.of(map);
        assertFalse(columns.isEmpty());
        assertThat(columns, Matchers.contains(Column.of("name", "Ada")));
    }


    @Test
    public void shouldCreateRecursiveMap() {
        List<List<Map<String, String>>> list = new ArrayList<>();
        Map<String, String> map = singletonMap("mobile", "55 1234-4567");
        list.add(singletonList(map));

        List<Column> columns = Columns.of(singletonMap("contact", list));
        assertEquals(1, columns.size());
        Column column = columns.get(0);
        assertEquals("contact", column.getName());
        List<List<Column>> result = (List<List<Column>>) column.get();
        assertEquals(Column.of("mobile", "55 1234-4567"), result.get(0).get(0));

    }
}