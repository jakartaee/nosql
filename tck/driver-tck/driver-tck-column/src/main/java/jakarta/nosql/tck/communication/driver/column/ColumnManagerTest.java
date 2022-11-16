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
package jakarta.nosql.tck.communication.driver.column;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnManager;
import jakarta.nosql.column.ColumnQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static jakarta.nosql.column.ColumnDeleteQuery.delete;
import static jakarta.nosql.column.ColumnQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class ColumnManagerTest {

    @BeforeEach
    public void setUp() {
        Module module = ColumnManagerTest.class.getModule();
        module.addUses(ColumnManagerSupplier.class);
    }
    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldInsert(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        Optional<ColumnEntity> entityOptional = argument.insertOne(manager);

        Assertions.assertTrue(entityOptional.isPresent());
        final ColumnEntity entity = entityOptional
                .orElseThrow(() -> new ColumnDriverException("Should return an entity when the entity is saved"));

        final Column id = entity.find(argument.getIdName())
                .orElseThrow(() -> new ColumnDriverException("Should return the id in the entity"));
        ColumnDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnErrorWhenInsertIsNull(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.insert((ColumnEntity) null));
    }

    @ParameterizedTest
    @ColumnSource("column_ttl.properties")
    public void shouldInsertTTL(ColumnArgument argument) throws InterruptedException {
        ColumnManager manager = getManager(argument);
        Optional<ColumnEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final ColumnEntity entity = entityOptional
                .orElseThrow(() -> new ColumnDriverException("Should return an entity when the entity is saved"));

        final Column id = entity.find(argument.getIdName())
                .orElseThrow(() -> new ColumnDriverException("Should return the id in the entity"));

        TimeUnit.SECONDS.sleep(2L);
        final ColumnQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        final long count = manager.select(query).count();
        assertEquals(0L, count);
    }

    @ParameterizedTest
    @ColumnSource("column_ttl.properties")
    public void shouldReturnErrorWhenInsertTTLHasNullParameter(ColumnArgument argument) throws InterruptedException {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class,
                () -> manager.insert((ColumnEntity) null, Duration.ZERO));
        assertThrows(NullPointerException.class,
                () -> manager.insert(ColumnEntity.of("entity"), null));
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldInsertIterable(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        List<ColumnEntity> entities = argument.insertAll(manager);

        final List<Object> ids = entities.stream()
                .map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Column::get)
                .collect(Collectors.toList());

        assertEquals(argument.getQuery().size(), ids.size());

        ColumnDeleteQuery deleteQuery = delete().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnErrorWhenInsertIterableIsNull(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.insert((Iterable<ColumnEntity>) null));
    }

    @ParameterizedTest
    @ColumnSource("column_ttl.properties")
    public void shouldInsertIterableTTL(ColumnArgument argument) throws InterruptedException {
        ColumnManager manager = getManager(argument);
        List<ColumnEntity> entities = argument.insertAll(manager);
        Assertions.assertEquals(argument.getQuery().size(), entities.size());

        final List<Object> ids = entities.stream().map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get).map(Column::get).collect(Collectors.toList());

        TimeUnit.SECONDS.sleep(2L);
        final ColumnQuery query = select().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        final long count = manager.select(query).count();
        assertEquals(0L, count);
    }

    @ParameterizedTest
    @ColumnSource("column_ttl.properties")
    public void shouldReturnErrorWhenInsertIterableTTL(ColumnArgument argument) throws InterruptedException {
        ColumnManager manager = getManager(argument);

        assertThrows(NullPointerException.class, () -> manager.insert((Iterable<ColumnEntity>) null,
                null));
        assertThrows(NullPointerException.class, () -> manager.insert((Iterable<ColumnEntity>) null,
                Duration.ZERO));
        assertThrows(NullPointerException.class, () -> manager
                .insert(Collections.singletonList(ColumnEntity.of("entity")),
                        null));
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldUpdate(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        Optional<ColumnEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final ColumnEntity entity = entityOptional
                .orElseThrow(() -> new ColumnDriverException("Should return an entity when the entity is saved"));
        assertNotNull(manager.update(entity));
        final Column id = entity.find(argument.getIdName())
                .orElseThrow(() -> new ColumnDriverException("Should return the id in the entity"));
        ColumnDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnErrorWhenUpdateIsNull(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.update((ColumnEntity) null));
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldUpdateIterable(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        List<ColumnEntity> entities = argument.insertAll(manager);

        assertNotNull(manager.update(entities));
        final List<Object> ids = entities.stream()
                .map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Column::get)
                .collect(Collectors.toList());

        assertEquals(argument.getQuery().size(), ids.size());

        ColumnDeleteQuery deleteQuery = delete().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnErrorWhenUpdateIterableIsNull(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.update((Iterable<ColumnEntity>) null));
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldDelete(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        Optional<ColumnEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final ColumnEntity entity = entityOptional
                .orElseThrow(() -> new ColumnDriverException("Should return an entity when the entity is saved"));

        final Column id = entity.find(argument.getIdName())
                .orElseThrow(() -> new ColumnDriverException("Should return the id in the entity"));
        ColumnDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);

        ColumnQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        assertEquals(0L, manager.select(query).count());
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnErrorWhenDelete(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.delete(null));
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldSelect(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        Optional<ColumnEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final ColumnEntity entity = entityOptional
                .orElseThrow(() -> new ColumnDriverException("Should return an entity when the entity is saved"));

        final Column id = entity.find(argument.getIdName())
                .orElseThrow(() -> new ColumnDriverException("Should return the id in the entity"));

        ColumnQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        assertEquals(1L, manager.select(query).count());
        ColumnDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnErrorWhenSelect(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.select(null));
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldSingleResult(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        Optional<ColumnEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final ColumnEntity entity = entityOptional
                .orElseThrow(() -> new ColumnDriverException("Should return an entity when the entity is saved"));

        final Column id = entity.find(argument.getIdName())
                .orElseThrow(() -> new ColumnDriverException("Should return the id in the entity"));

        ColumnQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        final Optional<ColumnEntity> optional = manager.singleResult(query);
        assertTrue(optional.isPresent());
        ColumnDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnAnErrorEmptySingleResult(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        List<ColumnEntity> entities = argument.insertAll(manager);

        assertNotNull(manager.update(entities));
        final List<Object> ids = entities.stream()
                .map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Column::get)
                .collect(Collectors.toList());

        ColumnQuery query = select().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();

        assertThrows(NonUniqueResultException.class, () -> manager.singleResult(query));
        ColumnDeleteQuery deleteQuery = delete().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @ColumnSource("column.properties")
    public void shouldReturnErrorWhenSingleResult(ColumnArgument argument) {
        ColumnManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.singleResult(null));
    }

    private ColumnManager getManager(ColumnArgument argument) {
        assumeTrue(argument.isEmpty());
        return getManager();
    }

    private ColumnManager getManager() {
        final ColumnManagerSupplier supplier = ServiceLoaderProvider
                .get(ColumnManagerSupplier.class,
                        ()-> ServiceLoader.load(ColumnManagerSupplier.class));
        return supplier.get();
    }

}
