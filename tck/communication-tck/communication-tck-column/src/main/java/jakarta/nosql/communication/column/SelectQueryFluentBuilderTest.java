/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.TypeReference;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jakarta.nosql.column.ColumnCondition.eq;
import static jakarta.nosql.column.ColumnQuery.select;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectQueryFluentBuilderTest {

    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        Assertions.assertThrows(NullPointerException.class, () -> select("column", "column", null));
    }

    @Test
    public void shouldSelect() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }

    @Test
    public void shouldSelectColumns() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select("column", "column2").from(columnFamily).build();
        assertThat(query.getColumns()).contains("column", "column2");
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }

    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> select().from(null));
    }


    @Test
    public void shouldSelectOrderAsc() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).orderBy("name").asc().build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertThat(query.getSorts()).contains(Sort.of("name", SortType.ASC));
    }

    @Test
    public void shouldSelectOrderDesc() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).orderBy("name").desc().build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertThat(query.getSorts()).contains(Sort.of("name", SortType.DESC));
    }

    @Test
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            String columnFamily = "columnFamily";
            select().from(columnFamily).orderBy(null);
        });
    }

    @Test
    public void shouldSelectLimit() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).limit(10).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(10L, query.getLimit());
    }

    @Test
    public void shouldReturnErrorWhenLimitIsNegative() {
        String columnFamily = "columnFamily";
        Assertions.assertThrows(IllegalArgumentException.class, () -> select().from(columnFamily).limit(-1));
    }


    @Test
    public void shouldSelectSkip() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).skip(10).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(10L, query.getSkip());
    }

    @Test
    public void shouldReturnErrorWhenSkipIsNegative() {
        String columnFamily = "columnFamily";
        Assertions.assertThrows(IllegalArgumentException.class, () -> select().from(columnFamily).skip(-1));
    }

    @Test
    public void shouldSelectWhereNameEq() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").eq(name).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(name, column.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").like(name).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.LIKE, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(name, column.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").gt(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.GREATER_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").gte(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").lt(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.LESSER_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").lte(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String columnFamily = "columnFamily";
        Number valueA = 10;
        Number valueB = 20;
        ColumnQuery query = select().from(columnFamily).where("name").between(valueA, valueB).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.BETWEEN, condition.getCondition());
        assertEquals("name", column.getName());
        assertThat(column.get(new TypeReference<List<Number>>() {
        })).contains(10, 20);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").not().eq(name).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        ColumnCondition negate = column.get(ColumnCondition.class);
        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.NOT, condition.getCondition());
        assertEquals(Condition.EQUALS, negate.getCondition());
        assertEquals("name", negate.getColumn().getName());
        assertEquals(name, negate.getColumn().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").eq(name).and("age").gt(10).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions).contains(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10)));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").eq(name).or("age").gt(10).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions).contains(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10)));
    }

    @Test
    public void shouldSelectNegate() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).where("city").not().eq("Assis")
                .and("name").not().eq("Lucas").build();

        ColumnCondition condition = query.getCondition().orElseThrow(RuntimeException::new);
        assertEquals(columnFamily, query.getColumnFamily());
        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });

        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions).contains(eq(Column.of("city", "Assis")).negate(),
                eq(Column.of("name", "Lucas")).negate());


    }

    @Test
    public void shouldExecuteManager() {
        ColumnFamilyManager manager = Mockito.mock(ColumnFamilyManager.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String columnFamily = "columnFamily";
        Stream<ColumnEntity> entities = select().from(columnFamily).getResult(manager);
        entities.collect(Collectors.toList());
        Mockito.verify(manager).select(queryCaptor.capture());
        checkQuery(queryCaptor, columnFamily);
    }

    @Test
    public void shouldExecuteSingleResultManager() {
        ColumnFamilyManager manager = Mockito.mock(ColumnFamilyManager.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String columnFamily = "columnFamily";
        Optional<ColumnEntity> entities = select().from(columnFamily).getSingleResult(manager);
        Mockito.verify(manager).singleResult(queryCaptor.capture());
        checkQuery(queryCaptor, columnFamily);
    }

    private void checkQuery(ArgumentCaptor<ColumnQuery> queryCaptor, String columnFamily) {
        ColumnQuery query = queryCaptor.getValue();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }

}