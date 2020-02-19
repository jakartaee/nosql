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

import jakarta.nosql.Condition;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnPreparedStatement;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ColumnQueryParserTest {
    
    private ColumnQueryParser parser = ServiceLoaderProvider.get(ColumnQueryParser.class);

    private ColumnFamilyManager manager = mock(ColumnFamilyManager.class);

    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {
        assertThrows(NullPointerException.class, () -> parser.query(null, manager, ColumnObserverParser.EMPTY));
        assertThrows(NullPointerException.class, () -> parser.query("select * from God", null, ColumnObserverParser.EMPTY));
    }

    @Test
    public void shouldReturnErrorWhenHasInvalidQuery() {
        assertThrows(QueryException.class, () -> parser.query("inva", manager, ColumnObserverParser.EMPTY));
        assertThrows(QueryException.class, () -> parser.query("invalid", manager, ColumnObserverParser.EMPTY));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.getColumns().isEmpty());
        assertTrue(columnQuery.getSorts().isEmpty());
        assertEquals(0L, columnQuery.getLimit());
        assertEquals(0L, columnQuery.getSkip());
        assertEquals("God", columnQuery.getColumnFamily());
        assertFalse(columnQuery.getCondition().isPresent());

    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        parser.query(query, manager, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).delete(captor.capture());
        ColumnDeleteQuery columnDeleteQuery = captor.getValue();

        assertTrue(columnDeleteQuery.getColumns().isEmpty());
        assertEquals("God", columnDeleteQuery.getColumnFamily());
        assertFalse(columnDeleteQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery2(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        parser.query(query, manager, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).insert(captor.capture());
        ColumnEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        parser.query(query, manager, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).update(captor.capture());
        ColumnEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);

        ColumnPreparedStatement prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult();
        Mockito.verify(manager).delete(captor.capture());
        ColumnDeleteQuery columnDeleteQuery = captor.getValue();
        ColumnCondition columnCondition = columnDeleteQuery.getCondition().get();
        Column column = columnCondition.getColumn();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    public void shouldExecutePrepareStatement1(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ColumnPreparedStatement prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult();
        Mockito.verify(manager).insert(captor.capture());
        ColumnEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatement2(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);

        ColumnPreparedStatement prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult();
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();
        ColumnCondition columnCondition = columnQuery.getCondition().get();
        Column column = columnCondition.getColumn();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldExecutePrepareStatement3(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ColumnPreparedStatement prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult();
        Mockito.verify(manager).update(captor.capture());
        ColumnEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldSingleResult(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);

        Mockito.when(manager.select(Mockito.any(ColumnQuery.class)))
                .thenReturn(Stream.of(mock(ColumnEntity.class)));

        ColumnPreparedStatement prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("age", 12);
        final Optional<ColumnEntity> result = prepare.getSingleResult();
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();
        ColumnCondition columnCondition = columnQuery.getCondition().get();
        Column column = columnCondition.getColumn();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
        assertTrue(result.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnEmptySingleResult(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);

        Mockito.when(manager.select(Mockito.any(ColumnQuery.class)))
                .thenReturn(Stream.empty());

        ColumnPreparedStatement prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("age", 12);
        final Optional<ColumnEntity> result = prepare.getSingleResult();
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();
        ColumnCondition columnCondition = columnQuery.getCondition().get();
        Column column = columnCondition.getColumn();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
        assertFalse(result.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnErrorSingleResult(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);

        Mockito.when(manager.select(Mockito.any(ColumnQuery.class)))
                .thenReturn(Stream.of(mock(ColumnEntity.class), mock(ColumnEntity.class)));

        ColumnPreparedStatement prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("age", 12);
       assertThrows(NonUniqueResultException.class, prepare::getSingleResult);
    }

}
