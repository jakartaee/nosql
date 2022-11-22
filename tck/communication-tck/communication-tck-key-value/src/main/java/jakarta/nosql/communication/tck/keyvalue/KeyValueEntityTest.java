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
package jakarta.nosql.communication.tck.keyvalue;

import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.KeyValueEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KeyValueEntityTest {


    @Test
    public void shouldReturnErrorWhenKeyIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> KeyValueEntity.of(null, "value"));
    }

    @Test
    public void shouldReturnErrorWhenValueIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> KeyValueEntity.of("key", null));
    }

    @Test
    public void shouldCreateInstance() {
        KeyValueEntity entity = KeyValueEntity.of("key", "value");
        assertNotNull(entity);
        assertEquals("key", entity.getKey());
        assertEquals("value", entity.getValue());
    }

    @Test
    public void shouldAliasOnValue() {
        String value = "10";
        KeyValueEntity entity = KeyValueEntity.of("key", value);
        assertEquals(value, entity.getValue());
        assertEquals(Integer.valueOf(10), entity.getValue(Integer.class));
        assertThat(singletonList(10)).hasSize(1).contains(entity.getValue(new TypeReference<List<Integer>>() {
        }).get(0));
    }

    @Test
    public void shouldGetValue() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("key", value);
        assertNotNull(entity);
        assertEquals("value", entity.getValue());
    }


    @Test
    public void shouldGetKeyClass() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        assertEquals(Long.valueOf(10L), entity.getKey(Long.class));
    }


    @Test
    public void shouldReturnErrorWhenGetKeyClassIsNull() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        Assertions.assertThrows(NullPointerException.class, () -> entity.getKey((Class<Object>) null));
    }


    @Test
    public void shouldGetKeyValueSupplier() {
        String value = "10";
        KeyValueEntity entity = KeyValueEntity.of(value, value);
        assertEquals(value, entity.getValue());
        assertEquals(Integer.valueOf(10), entity.getKey(Integer.class));
        assertThat(singletonList(10)).contains(entity.getValue(new TypeReference<List<Integer>>() {
        }).get(0));
    }

    @Test
    public void shouldReturnErrorWhenGetKeySupplierIsNull() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        Assertions.assertThrows(NullPointerException.class, () -> entity.getKey((TypeReference<Object>) null));
    }
}