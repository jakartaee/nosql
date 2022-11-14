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
package jakarta.nosql.communication.tck;

import jakarta.nosql.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SettingsTest {

    @Test
    public void shouldReturnNPEWhenInstanceIsNull() {
        assertThrows(NullPointerException.class, () -> Settings.of((Map<String, Object>) null));

    }

    @Test
    public void shouldReturnNewInstance() {
        Settings settings = Settings.of();
        assertTrue(settings.isEmpty());
        assertEquals(0, settings.size());
    }

    @Test
    public void shouldCreateFromMap() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertFalse(settings.isEmpty());
        assertEquals(1, settings.size());
        assertEquals("value", settings.get("key")
                .orElseThrow(() -> new NoSuchElementException("There is not key element in the settings")));
    }

    @Test
    public void shouldContainsKeys() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertTrue(settings.containsKey("key"));
        assertFalse(settings.containsKey("key2"));
    }


    @Test
    public void shouldGetKeys() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertThat(settings.keySet()).hasSize(1).contains("key");
    }

    @Test
    public void shouldGetEntrySet() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertEquals(settings.entrySet().stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("The first element was not found"))
                .getKey(), "key");
    }

    @Test
    public void shouldSize() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertEquals(1, settings.size());
        settings = Settings.of(Collections.emptyMap());
        assertEquals(0, settings.size());
    }

    @Test
    public void shouldIsEmpty() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertFalse(settings.isEmpty());
        settings = Settings.of(Collections.emptyMap());
        assertTrue(settings.isEmpty());
    }

    @Test
    public void shouldGetValueClass() {
        Settings settings = Settings.of(singletonMap("key", "12"));

        Integer value = settings.get("key", Integer.class)
                .orElseThrow(() -> new NoSuchElementException("There is not 'key' element in the settings"));
        assertEquals(Integer.valueOf(12), value);
        assertFalse(settings.get("key2", Integer.class).isPresent());
    }

    @Test
    public void shouldIterateUsingForEach() {
        Settings settings = Settings.of(singletonMap("key", "12"));
        List<Map.Entry<String, Object>> references = new ArrayList<>();
        settings.forEach((k, v) -> references.add(new AbstractMap.SimpleEntry<>(k, v)));

        assertFalse(references.isEmpty());
        Map.Entry<String, Object> entry = references.get(0);
        Assertions.assertEquals("key", entry.getKey());
        Assertions.assertEquals("12", entry.getValue());
    }

    @Test
    public void shouldGetOrDefault() {
        Settings settings = Settings.of(singletonMap("key", "12"));
        assertEquals("12", settings.getOrDefault("key", "13"));
        assertEquals("13", settings.getOrDefault("key-1", "13"));
    }

    @Test
    public void shouldReturnErrorWhenPrefixIsNull() {
        Settings settings = Settings.builder()
                .put("host", "host")
                .put("host-1", "host-1")
                .put("host-2", "host-2")
                .put("host-3", "host-3")
                .build();
        assertThrows(NullPointerException.class, () -> settings.prefix((String) null));
    }

    @Test
    public void shouldFindPrefix() {
        Settings settings = Settings.builder()
                .put("host", "host")
                .put("host-1", "host-1")
                .put("host-2", "host-2")
                .put("host-3", "host-3")
                .build();

        List<Object> hosts = settings.prefix("host");
        Assertions.assertEquals(4, hosts.size());
        assertThat(hosts).hasSize(4).contains("host", "host-1", "host-2", "host-3");
    }

    @Test
    public void shouldFindPrefixWithOrder() {
        Settings settings = Settings.builder()
                .put("host", "host")
                .put("host-3", "host-3")
                .put("host-2", "host-2")
                .put("host-1", "host-1")
                .build();

        List<Object> hosts = settings.prefix("host");
        assertThat(hosts).hasSize(4).contains("host", "host-1", "host-2", "host-3");
    }


    @Test
    public void shouldReturnErrorWhenPrefixesIsNull() {
        Settings settings = Settings.builder()
                .put("host", "host")
                .put("host-1", "host-1")
                .put("host-2", "host-2")
                .put("host-3", "host-3")
                .build();
        assertThrows(NullPointerException.class, () -> settings.prefix((Collection<String>) null));
    }

    @Test
    public void shouldFindPrefixes() {
        Settings settings = Settings.builder()
                .put("host", "host")
                .put("host-1", "host-1")
                .put("server", "server")
                .put("server-1", "server-1")
                .build();

        List<Object> hosts = settings.prefix(Arrays.asList("host", "server"));
        assertThat(hosts).hasSize(4).contains("host", "host-1", "server", "server-1");

    }

    @Test
    public void shouldFindPrefixesSort() {
        Settings settings = Settings.builder()
                .put("host-1", "host-1")
                .put("host", "host")
                .put("server-1", "server-1")
                .put("server", "server")
                .build();

        List<Object> hosts = settings.prefix(Arrays.asList("host", "server"));
        assertThat(hosts).hasSize(4).contains("host", "host-1", "server", "server-1");
    }

}
