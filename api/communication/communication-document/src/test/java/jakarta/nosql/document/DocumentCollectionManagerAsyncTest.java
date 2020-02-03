/*
 * Copyright (c) 2019 Otavio Santana and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package jakarta.nosql.document;

import jakarta.nosql.NonUniqueResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentCollectionManagerAsyncTest {

    @Spy
    private DocumentCollectionManagerAsync manager;

    @Test
    public void shouldReturnSingleResult() {
        DocumentEntity entity = Mockito.mock(DocumentEntity.class);
        DocumentQuery query = Mockito.mock(DocumentQuery.class);
        ArgumentCaptor<Consumer<Stream<DocumentEntity>>> callBack =
                ArgumentCaptor.forClass(Consumer.class);

        AtomicReference<Optional<DocumentEntity>> reference = new AtomicReference<>();
        Consumer<Optional<DocumentEntity>> singleCallBack = reference::set;

        manager.singleResult(query, singleCallBack);

        Mockito.verify(manager).select(Mockito.eq(query), callBack.capture());
        final Consumer<Stream<DocumentEntity>> consumer = callBack.getValue();
        consumer.accept(Stream.of(entity));

        Optional<DocumentEntity> result = reference.get();
        Assertions.assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void shouldReturnEmptyOptionalAtSingleResult() {
        DocumentQuery query = Mockito.mock(DocumentQuery.class);
        ArgumentCaptor<Consumer<Stream<DocumentEntity>>> callBack =
                ArgumentCaptor.forClass(Consumer.class);

        AtomicReference<Optional<DocumentEntity>> reference = new AtomicReference<>();
        Consumer<Optional<DocumentEntity>> singleCallBack = reference::set;

        manager.singleResult(query, singleCallBack);

        Mockito.verify(manager).select(Mockito.eq(query), callBack.capture());
        final Consumer<Stream<DocumentEntity>> consumer = callBack.getValue();
        consumer.accept(Stream.empty());

        Optional<DocumentEntity> result = reference.get();
        Assertions.assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnErrorWhenReturnMoreThanOneSingleResult() {
        DocumentEntity entity = Mockito.mock(DocumentEntity.class);
        DocumentQuery query = Mockito.mock(DocumentQuery.class);
        ArgumentCaptor<Consumer<Stream<DocumentEntity>>> callBack =
                ArgumentCaptor.forClass(Consumer.class);

        AtomicReference<Optional<DocumentEntity>> reference = new AtomicReference<>();
        Consumer<Optional<DocumentEntity>> singleCallBack = reference::set;

        manager.singleResult(query, singleCallBack);

        Mockito.verify(manager).select(Mockito.eq(query), callBack.capture());
        final Consumer<Stream<DocumentEntity>> consumer = callBack.getValue();
        assertThrows(NonUniqueResultException.class, () -> consumer.accept(Stream.of(entity, entity)));
    }

    @Test
    public void shouldReturnErrorWhenReturnsNull() {
        AtomicReference<Optional<DocumentEntity>> reference = new AtomicReference<>();
        Consumer<Optional<DocumentEntity>> singleCallBack = reference::set;
        assertThrows(NullPointerException.class, () -> manager.singleResult(null, singleCallBack));
    }
}