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
package jakarta.nosql.tck.communication.driver.document;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static jakarta.nosql.document.DocumentDeleteQuery.delete;
import static jakarta.nosql.document.DocumentQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class DocumentCollectionManagerTest {

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldInsert(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        Optional<DocumentEntity> entityOptional = argument.getQuery().stream().flatMap(manager::query)
                .findFirst();
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
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class, () -> manager.insert((DocumentEntity) null));
    }

    @ParameterizedTest
    @DocumentSource("document_ttl.properties")
    public void shouldInsertTTL(DocumentArgument argument) throws InterruptedException {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        Optional<DocumentEntity> entityOptional = argument.getQuery().stream().flatMap(manager::query)
                .findFirst();
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
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class,
                () -> manager.insert((DocumentEntity) null, Duration.ZERO));
        assertThrows(NullPointerException.class,
                () -> manager.insert(DocumentEntity.of("entity"), null));
    }

    @ParameterizedTest
    @DocumentSource("document_iterable.properties")
    public void shouldInsertIterable(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
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
    @DocumentSource("document_iterable.properties")
    public void shouldReturnErrorWhenInsertIterableIsNull(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class, () -> manager.insert((Iterable<DocumentEntity>) null));
    }

    @ParameterizedTest
    @DocumentSource("document_iterable_ttl.properties")
    public void shouldInsertIterableTTL(DocumentArgument argument) throws InterruptedException {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        List<DocumentEntity> entities = argument.getQuery().stream().flatMap(manager::query)
                .collect(Collectors.toList());
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
    @DocumentSource("document_iterable_ttl.properties")
    public void shouldReturnErrorWhenInsertIterableTTL(DocumentArgument argument) throws InterruptedException {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();

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
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        Optional<DocumentEntity> entityOptional = argument.getQuery().stream().flatMap(manager::query)
                .findFirst();
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
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class, () -> manager.update((DocumentEntity) null));
    }

    @ParameterizedTest
    @DocumentSource("document_iterable.properties")
    public void shouldUpdateIterable(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        List<DocumentEntity> entities = argument.getQuery().stream().flatMap(manager::query)
                .collect(Collectors.toList());

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
    @DocumentSource("document_iterable.properties")
    public void shouldReturnErrorWhenUpdateIterableIsNull(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class, () -> manager.update((Iterable<DocumentEntity>) null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldDelete(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        Optional<DocumentEntity> entityOptional = argument.getQuery().stream().flatMap(manager::query)
                .findFirst();
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
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class, () -> manager.delete(null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldSelect(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        Optional<DocumentEntity> entityOptional = argument.getQuery().stream().flatMap(manager::query)
                .findFirst();
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));

        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));

        DocumentQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        assertEquals(1L, manager.select(query).count());
        DocumentDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenSelect(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class, () -> manager.select(null));
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldSingleResult(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        Optional<DocumentEntity> entityOptional = argument.getQuery().stream().flatMap(manager::query)
                .findFirst();
        Assertions.assertTrue(entityOptional.isPresent());
        final DocumentEntity entity = entityOptional
                .orElseThrow(() -> new DocumentDriverException("Should return an entity when the entity is saved"));

        final Document id = entity.find(argument.getIdName())
                .orElseThrow(() -> new DocumentDriverException("Should return the id in the entity"));

        DocumentQuery query = select().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        final Optional<DocumentEntity> optional = manager.singleResult(query);
        assertTrue(optional.isPresent());
        DocumentDeleteQuery deleteQuery = delete().from(entity.getName()).where(id.getName()).eq(id.get()).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document_iterable.properties")
    public void shouldAnEmptySingleResult(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        List<DocumentEntity> entities = argument.getQuery().stream().flatMap(manager::query)
                .collect(Collectors.toList());

        assertNotNull(manager.update(entities));
        final List<Object> ids = entities.stream()
                .map(c -> c.find(argument.getIdName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Document::get)
                .collect(Collectors.toList());

        DocumentQuery query = select().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();

        assertThrows(NonUniqueResultException.class, () -> manager.singleResult(query));
        DocumentDeleteQuery deleteQuery = delete().from(entities.get(0).getName())
                .where(argument.getIdName()).in(ids).build();
        manager.delete(deleteQuery);
    }

    @ParameterizedTest
    @DocumentSource("document.properties")
    public void shouldReturnErrorWhenSingleResult(DocumentArgument argument) {
        assumeTrue(argument.isEmpty());
        DocumentCollectionManager manager = getManager();
        assertThrows(NullPointerException.class, () -> manager.singleResult(null));
    }

    private DocumentCollectionManager getManager() {
        final DocumentCollectionManagerSupplier supplier = ServiceLoaderProvider.get(DocumentCollectionManagerSupplier.class);
        return supplier.get();
    }

}
