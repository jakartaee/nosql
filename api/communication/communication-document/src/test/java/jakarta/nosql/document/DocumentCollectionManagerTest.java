/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DocumentCollectionManagerTest {

    @Spy
    private DocumentCollectionManager manager;

    @Test
    public void shouldReturnSingleResult() {
        DocumentEntity entity = Mockito.mock(DocumentEntity.class);
        DocumentQuery query = Mockito.mock(DocumentQuery.class);
        Mockito.when(manager.select(Mockito.any(DocumentQuery.class)))
                .thenReturn(Stream.of(entity));

        Optional<DocumentEntity> result = manager.singleResult(query);
        Assertions.assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());

    }

    @Test
    public void shouldReturnEmptyOptionalAtSingleResult() {
        DocumentQuery query = Mockito.mock(DocumentQuery.class);
        Mockito.when(manager.select(Mockito.any(DocumentQuery.class)))
                .thenReturn(Stream.empty());

        Optional<DocumentEntity> result = manager.singleResult(query);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnErrorWhenReturnMoreThanOneSingleResult() {
        DocumentEntity entity = Mockito.mock(DocumentEntity.class);
        DocumentQuery query = Mockito.mock(DocumentQuery.class);
        Mockito.when(manager.select(Mockito.any(DocumentQuery.class)))
                .thenReturn(Stream.of(entity, entity));
        assertThrows(NonUniqueResultException.class, () ->  manager.singleResult(query));
    }

    @Test
    public void shouldReturnErrorWhenReturnsNull() {
        assertThrows(NullPointerException.class, () ->  manager.singleResult(null));
    }

}