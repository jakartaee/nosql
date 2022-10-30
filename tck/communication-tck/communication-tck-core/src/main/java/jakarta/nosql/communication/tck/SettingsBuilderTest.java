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

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SettingsBuilderTest {

    @Test
    public void shouldReturnErrorWhenKeyIsNUll() {
        Assertions.assertThrows(NullPointerException.class, () -> Settings.builder().put(null, "value"));
    }

    @Test
    public void shouldReturnErrorWhenValueIsNUll() {
        Assertions.assertThrows(NullPointerException.class, () -> Settings.builder().put("key", null));
    }

    @Test
    public void shouldReturnErrorWhenMapHasNullKey() {

        Assertions.assertThrows(NullPointerException.class, () -> {
            Map<String, Object> map = Collections.singletonMap(null, "value");
            Settings.builder().putAll(map);
        });
    }

    @Test
    public void shouldReturnErrorWhenMapHasNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> {

            Map<String, Object> map = Collections.singletonMap("key", null);
            Settings.builder().putAll(map);
        });
    }

    @Test
    public void shouldCreateSettingsBuilder() {
        Settings settings = Settings.builder().put("key", "value").build();
        assertNotNull(settings);
        assertEquals("value", settings.get("key")
                .orElseThrow(() -> new NoSuchElementException("There is not 'key' element in the settings")));
    }
}