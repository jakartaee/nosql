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

import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.column.ColumnQueryPagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jakarta.nosql.column.ColumnQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ColumnQueryPaginationTest {


    @Test
    public void shouldReturnNPEWhenQueryIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ColumnQueryPagination.of(null, Pagination.page(1).size(2)));
    }

    @Test
    public void shouldReturnNPEWhenPaginationIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ColumnQueryPagination.of(select().from("column").build(), null));
    }

    @Test
    public void shouldCreateColumnQueryPagination() {
        ColumnQuery query = select().from("column").build();
        Pagination pagination = Pagination.page(1).size(2);
        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);

        isQueryEquals(query, pagination, queryPagination);
    }

    @Test
    public void shouldOverrideSkipLimit() {

        ColumnQuery query = select().from("column").build();
        Pagination pagination = Pagination.page(1).size(2);
        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);
        assertEquals(pagination.getLimit(), queryPagination.getLimit());
        assertEquals(pagination.getSkip(), queryPagination.getSkip());

    }

    @Test
    public void shouldNext() {
        ColumnQuery query = select().from("column").where("name").eq("Ada").build();
        Pagination pagination = Pagination.page(1).size(2);
        Pagination secondPage = pagination.next();

        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);
        assertEquals(pagination.getLimit(), queryPagination.getLimit());
        assertEquals(pagination.getSkip(), queryPagination.getSkip());

        isQueryEquals(query, pagination, queryPagination);

        ColumnQueryPagination next = queryPagination.next();

        isQueryEquals(query, secondPage, next);
    }


    private void isQueryEquals(ColumnQuery query, Pagination pagination, ColumnQueryPagination queryPagination) {
        assertEquals(query.getColumnFamily(), queryPagination.getColumnFamily());
        assertEquals(query.getColumns(), queryPagination.getColumns());
        assertEquals(pagination, queryPagination.getPagination());

        assertEquals(query.getSorts(), queryPagination.getSorts());
        assertEquals(query.getCondition().orElse(null), queryPagination.getCondition().orElse(null));
    }

}