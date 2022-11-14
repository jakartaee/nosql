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


import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ValueTest {

    @Test
    public void shouldReturnErrorWhenElementIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Value.of(null));

    }

    @Test
    public void shouldReturnSameInstanceInGet() {
        AtomicInteger number = new AtomicInteger(5_000);
        Value value = Value.of(number);
        assertEquals(number, value.get());
    }

    @Test
    public void shouldConvertType() {
        AtomicInteger number = new AtomicInteger(5_000);
        Value value = Value.of(number);
        assertEquals(Integer.valueOf(5_000), value.get(Integer.class));
        assertEquals("5000", value.get(String.class));
    }

    @Test
    public void shouldConvertToSingletonList() {
        Long number = 10L;
        Value value = Value.of(number);

        assertThat(value.get(new TypeReference<List<String>>() { }))
                .hasSize(1).contains("10");
        assertThat(value.get(new TypeReference<List<Long>>() {
        })).hasSize(1).contains(10L);
    }

    @Test
    public void shouldConvertToStream() {
        Long number = 10L;
        Value value = Value.of(number);

        assertThat(value.get(new TypeReference<Stream<String>>() {
                }).collect(Collectors.toList()))
                .hasSize(1).contains("10");

        assertThat(value.get(new TypeReference<Stream<Long>>() {
        }).collect(Collectors.toList()))
                .hasSize(1).contains(10L);
    }

    @Test
    public void shouldConvertToList() {
        Value value = Value.of(Arrays.asList(10, 20, 30));
        assertThat(value.get(new TypeReference<List<String>>() {
        })).hasSize(3).contains("10", "20", "30");
        assertThat(value.get(new TypeReference<List<BigInteger>>() {
        })).hasSize(3).contains(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
    }

    @Test
    public void shouldConvertToSingletonSet() {
        Long number = 10L;
        Value value = Value.of(number);
        assertThat(value.get(new TypeReference<Set<String>>() {})).contains("10");
        assertThat(value.get(new TypeReference<Set<Long>>() {})).contains(10L);
    }

    @Test
    public void shouldConvertToSet() {
        Value value = Value.of(Arrays.asList(10, 20, 30));
        assertThat(value.get(new TypeReference<Set<String>>() {
        })).contains("10", "20", "30");
        assertThat(value.get(new TypeReference<List<BigInteger>>() {
        })).contains(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
    }

    @Test
    public void shouldConvertMap() {
        Map<String, Integer> map = Collections.singletonMap("ONE", 1);
        Value value = Value.of(map);

        Map<String, Integer> result = value.get(new TypeReference<>() {
        });

        assertThat(result.keySet()).hasSize(1).contains("ONE");
        assertThat(result.values()).hasSize(1).contains(1);
    }

    @Test
    public void shouldConvertKeyValueInsideMap() {
        Map<Integer, String> map = Collections.singletonMap(10, "1");
        Value value = Value.of(map);
        Map<String, Integer> result = value.get(new TypeReference<>() {
        });
        assertThat(result.keySet()).hasSize(1).contains("10");
        assertThat(result.values()).hasSize(1).contains(1);
    }

    @Test
    public void shouldConvertMapIgnoringKeyValue() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            Map<Integer, List<String>> map = Collections.singletonMap(10, Arrays.asList("1", "2", "3"));
            Value value = Value.of(map);
            Map<String, List<String>> result = value.get(new TypeReference<>() {
            });
            assertThat(result.keySet()).hasSize(1).contains("10");
            assertThat(result.values()).hasSize(1).contains(Arrays.asList("1", "2", "3"));
        });
    }

    @Test
    public void shouldReturnErrorWhenIsInstanceIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Value.of(12).isInstanceOf(null));
    }

    @Test
    public void shouldIsInstanceOf() {
        Assertions.assertTrue(Value.of("12").isInstanceOf(String.class));
        Assertions.assertFalse(Value.of("12").isInstanceOf(Integer.class));
    }
}