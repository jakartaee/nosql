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

package jakarta.nosql.column;

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
class ColumnFamilyManagerTest {

    @Spy
    private ColumnFamilyManager manager;

    @Test
    public void shouldReturnSingleResult() {
        ColumnEntity entity = Mockito.mock(ColumnEntity.class);
        ColumnQuery query = Mockito.mock(ColumnQuery.class);
        Mockito.when(manager.select(Mockito.any(ColumnQuery.class)))
                .thenReturn(Stream.of(entity));

        Optional<ColumnEntity> result = manager.singleResult(query);
        Assertions.assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());

    }

    @Test
    public void shouldReturnEmptyOptionalAtSingleResult() {
        ColumnQuery query = Mockito.mock(ColumnQuery.class);
        Mockito.when(manager.select(Mockito.any(ColumnQuery.class)))
                .thenReturn(Stream.empty());

        Optional<ColumnEntity> result = manager.singleResult(query);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnErrorAtSingleResult() {
        ColumnEntity entity = Mockito.mock(ColumnEntity.class);
        ColumnQuery query = Mockito.mock(ColumnQuery.class);
        Mockito.when(manager.select(Mockito.any(ColumnQuery.class)))
                .thenReturn(Stream.of(entity, entity));
        assertThrows(NonUniqueResultException.class, () ->  manager.singleResult(query));
    }

    @Test
    public void shouldReturnErrorWhenReturnsNull() {
        assertThrows(NullPointerException.class, () ->  manager.singleResult(null));
    }
}