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
package jakarta.nosql.tck.communication.driver.document;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.document.DocumentQuery;
import org.awaitility.Awaitility;
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

import static jakarta.nosql.document.DocumentDeleteQuery.delete;
import static jakarta.nosql.document.DocumentQuery.select;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class DocumentManagerTest {

    @BeforeEach
    public void setUp() {
        Module module = DocumentManagerTest.class.getModule();
        module.addUses(DocumentManagerSupplier.class);
        Awaitility.reset();
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldInsert(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        Optional<DocumentEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));

        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));
        DocumentDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenInsertIsNull(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.insert((DocumentEntity) null));
    }

    @ParameterizedTest
    @DocumentSource("document_ttl.properties")
    public void shouldInsertTTL(DocumentArgument argument) throws InterruptedException {
        DocumentManager manager = getManager(argument);
        Optional<DocumentEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));

        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));

        TimeUnit.SECONDS.sleep(2L);
        final DocumentQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        final long count = manager.select(query).count();
        assertEquals(0L, count);
    }

    @ParameterizedTest
    @DocumentSource("document_ttl.properties")
    public void shouldReturnErrorWhenInsertTTLHasNullParameter(DocumentArgument argument) throws InterruptedException {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class,
                () -> manager.insert((DocumentEntity) null, Duration.ZERO));
        assertThrows(NullPointerException.class,
                () -> manager.insert(DocumentEntity.of("entity"), null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldInsertIterable(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        List<DocumentEntity> entities = argument.getQuery().stream().flatMap(manager::query)
                .collect(Collectors.toList());

        final List<Object> ids = entities.stream()
                .map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Document::get)
                .collect(Collectors.toList());

        assertEquals(argument.getQuery().size(), ids.size());

        DocumentDeleteQuery deleteQuery = delete().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenInsertIterableIsNull(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.insert((Iterable<DocumentEntity>) null));
    }

    @ParameterizedTest
    @DocumentSource("document_ttl.properties")
    public void shouldInsertIterableTTL(DocumentArgument argument) throws InterruptedException {
        DocumentManager manager = getManager(argument);
        List<DocumentEntity> entities = argument.insertAll(manager);
        Assertions.assertEquals(argument.getQuery().size(), entities.size());

        final List<Object> ids = entities.stream().map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get).map(Document::get).collect(Collectors.toList());

        TimeUnit.SECONDS.sleep(2L);
        final DocumentQuery query = select().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        final long count = manager.select(query).count();
        assertEquals(0L, count);
    }

    @ParameterizedTest
    @DocumentSource("document_ttl.properties")
    public void shouldReturnErrorWhenInsertIterableTTL(DocumentArgument argument) throws InterruptedException {
        DocumentManager manager = getManager(argument);

        assertThrows(NullPointerException.class, () -> manager.insert((Iterable<DocumentEntity>) null,
                null));
        assertThrows(NullPointerException.class, () -> manager.insert((Iterable<DocumentEntity>) null,
                Duration.ZERO));
        assertThrows(NullPointerException.class, () -> manager
                .insert(Collections.singletonList(DocumentEntity.of("entity")),
                        null));
    }


    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldUpdate(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        Optional<DocumentEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));
        assertNotNull(manager.update(entity));
        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));
        DocumentDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenUpdateIsNull(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.update((DocumentEntity) null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldUpdateIterable(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        List<DocumentEntity> entities = argument.insertAll(manager);

        assertNotNull(manager.update(entities));
        final List<Object> ids = entities.stream()
                .map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Document::get)
                .collect(Collectors.toList());

        assertEquals(argument.getQuery().size(), ids.size());

        DocumentDeleteQuery deleteQuery = delete().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenUpdateIterableIsNull(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.update((Iterable<DocumentEntity>) null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldDelete(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        Optional<DocumentEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));

        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));
        DocumentDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);

        DocumentQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        assertEquals(0L, manager.select(query).count());
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenDelete(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.delete(null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldSelect(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        Optional<DocumentEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));

        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));

        DocumentQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();

        await().until(() -> manager.select(query).count(), equalTo(1L));

        DocumentDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenSelect(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.select(null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldSingleResult(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        Optional<DocumentEntity> entityOptional = argument.insertOne(manager);
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));

        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));

        DocumentQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();

        await().until(() -> manager.select(query).count(), greaterThan(0L));

        final Optional<DocumentEntity> optional = manager.singleResult(query);

        assertTrue(optional.isPresent());
        DocumentDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnAnErrorEmptySingleResult(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        List<DocumentEntity> entities = argument.insertAll(manager);

        assertNotNull(manager.update(entities));
        final List<Object> ids = entities.stream()
                .map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Document::get)
                .collect(Collectors.toList());

        DocumentQuery query = select().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();

        await().until(() -> manager.select(query).count(), equalTo((long) ids.size()));

        assertThrows(NonUniqueResultException.class, () -> manager.singleResult(query));

        DocumentDeleteQuery deleteQuery = delete().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenSingleResult(DocumentArgument argument) {
        DocumentManager manager = getManager(argument);
        assertThrows(NullPointerException.class, () -> manager.singleResult(null));
    }

    private DocumentManager getManager(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        return getManager();
    }

    private DocumentManager getManager() {
        final DocumentManagerSupplier supplier = ServiceLoaderProvider
                .get(DocumentManagerSupplier.class,
                        () -> ServiceLoader.load(DocumentManagerSupplier.class));
        return supplier.get();
    }

}
