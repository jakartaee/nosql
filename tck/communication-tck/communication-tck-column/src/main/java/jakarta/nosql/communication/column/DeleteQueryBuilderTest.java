/*
 *  Copyright (c) 2022 Otavio Santana and others
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


import jakarta.nosql.Condition;
import jakarta.nosql.TypeReference;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnFamilyManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static jakarta.nosql.column.ColumnCondition.eq;
import static jakarta.nosql.column.ColumnDeleteQuery.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteQueryBuilderTest {

    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        Assertions.assertThrows(NullPointerException.class,() -> builder("column", "column", null));
    }

    @Test
    public void shouldDelete() {
        String documentCollection = "documentCollection";
        ColumnDeleteQuery query = builder().from(documentCollection).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getColumnFamily());
    }

    @Test
    public void shouldDeleteDocuments() {
        String documentCollection = "documentCollection";
        ColumnDeleteQuery query = builder("document", "document2").from(documentCollection).build();
        assertThat(query.getColumns(), containsInAnyOrder("document", "document2"));
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getColumnFamily());
    }


    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        Assertions.assertThrows(NullPointerException.class,() -> builder().from(null));
    }

    @Test
    public void shouldSelectWhereNameEq() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.eq("name", name))
                .build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(name, column.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.like("name", name))
                .build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.LIKE, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(name, column.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.gt("name", value))
                .build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.GREATER_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        ColumnDeleteQuery query = builder()
                .from(documentCollection)
                .where(ColumnCondition.gte("name", value)).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.lt("name", value)).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.LESSER_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.lte("name", value)).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String documentCollection = "documentCollection";
        Number valueA = 10;
        Number valueB = 20;

        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.between("name", Arrays.asList(valueA, valueB)))
                .build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.BETWEEN, condition.getCondition());
        assertEquals("name", column.getName());
        assertThat(column.get(new TypeReference<List<Number>>() {}), Matchers.contains(10, 20));
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.eq("name", name).negate()).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        ColumnCondition negate = column.get(ColumnCondition.class);
        assertTrue(query.getColumns().isEmpty());
        assertEquals(documentCollection, query.getColumnFamily());
        assertEquals(Condition.NOT, condition.getCondition());
        assertEquals(Condition.EQUALS, negate.getCondition());
        assertEquals("name", negate.getColumn().getName());
        assertEquals(name, negate.getColumn().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        ColumnCondition nameEqualsAda = ColumnCondition.eq("name", name);
        ColumnCondition olderThenTen = ColumnCondition.gt("age", 10);
        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.and(nameEqualsAda, olderThenTen)).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<List<ColumnCondition>>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10))));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        ColumnCondition nameEqualsAda = ColumnCondition.eq("name", name);
        ColumnCondition olderThenTen = ColumnCondition.gt("age", 10);
        ColumnDeleteQuery query = builder().from(documentCollection)
                .where(ColumnCondition.or(nameEqualsAda, olderThenTen)).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<List<ColumnCondition>>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10))));
    }



    @Test
    public void shouldDeleteNegate() {
        String columnFamily = "columnFamily";
        ColumnCondition cityNotEqualsAssis = ColumnCondition.eq("city", "Assis").negate();
        ColumnCondition nameNotEqualsLucas = ColumnCondition.eq("name", "Lucas").negate();

        ColumnDeleteQuery query = builder().from(columnFamily)
                .where(ColumnCondition.and(cityNotEqualsAssis, nameNotEqualsLucas)).build();

        ColumnCondition condition = query.getCondition().orElseThrow(RuntimeException::new);
        assertEquals(columnFamily, query.getColumnFamily());
        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<List<ColumnCondition>>() {
        });

        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, containsInAnyOrder(eq(Column.of("city", "Assis")).negate(),
                eq(Column.of("name", "Lucas")).negate()));


    }

    @Test
    public void shouldExecuteDelete() {
        String collection = "collection";
        ColumnFamilyManager manager = mock(ColumnFamilyManager.class);
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        builder().from(collection).delete(manager);
        verify(manager).delete(queryCaptor.capture());

        ColumnDeleteQuery query = queryCaptor.getValue();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(collection, query.getColumnFamily());
    }
}
