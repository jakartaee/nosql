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
package jakarta.nosql.tck.communication.driver.keyvalue;


import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValueEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BucketManagerTest {


    private User userOtavio = new User("otavio");
    private KeyValueEntity keyValueOtavio = KeyValueEntity.of("otavio", Value.of(userOtavio));

    private User userSoro = new User("soro");
    private KeyValueEntity keyValueSoro = KeyValueEntity.of("soro", Value.of(userSoro));


    @BeforeEach
    public void setUp() {
        Module module = BucketManagerTest.class.getModule();
        module.addUses(BucketManagerSupplier.class);
    }
    @Test
    public void shouldPutValue() {
        final BucketManager manager = getBucketManager();
        manager.put("otavio", userOtavio);
        Optional<Value> otavio = manager.get("otavio");
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get().get(User.class));
    }

    @Test
    public void shouldPutKeyValue() {
        final BucketManager manager = getBucketManager();
        manager.put(keyValueOtavio);
        Optional<Value> otavio = manager.get("otavio");
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get().get(User.class));
    }

    @Test
    public void shouldPutIterableKeyValue() {
        final BucketManager manager = getBucketManager();
        manager.put(asList(keyValueSoro, keyValueOtavio));
        Optional<Value> otavio = manager.get("otavio");
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get().get(User.class));

        Optional<Value> soro = manager.get("soro");
        assertTrue(soro.isPresent());
        assertEquals(userSoro, soro.get().get(User.class));
    }

    @Test
    public void shouldMultiGet() {
        final BucketManager manager = getBucketManager();
        User user = new User("otavio");
        KeyValueEntity keyValue = KeyValueEntity.of("otavio", Value.of(user));
        manager.put(keyValue);
        assertNotNull(manager.get("otavio"));
    }

    @Test
    public void shouldRemoveKey() {
        final BucketManager manager = getBucketManager();
        manager.put(keyValueOtavio);
        assertTrue(manager.get("otavio").isPresent());
        manager.delete("otavio");
        assertFalse(manager.get("otavio").isPresent());
    }

    @Test
    public void shouldRemoveMultiKey() {
        final BucketManager manager = getBucketManager();
        manager.put(asList(keyValueSoro, keyValueOtavio));
        List<String> keys = asList("otavio", "soro");
        Iterable<Value> values = manager.get(keys);
        assertThat(StreamSupport.stream(values.spliterator(), false).map(value -> value.get(User.class))
                        .collect(Collectors.toList())).contains(userOtavio, userSoro);
        manager.delete(keys);
        assertEquals(0L, StreamSupport.stream(manager.get(keys).spliterator(), false).count());
    }

    @AfterEach
    public void remove() {
        final Optional<BucketManagerSupplier> supplier = getSupplier();
        if (supplier.isEmpty()) {
            final BucketManager manager = getBucketManager();
            manager.delete(Arrays.asList("otavio", "soro"));
        }
    }


    private BucketManager getBucketManager() {
        final BucketManagerSupplier bucketManagerSupplier = ServiceLoaderProvider
                .get(BucketManagerSupplier.class,
                        ()-> ServiceLoader.load(BucketManagerSupplier.class));
        return bucketManagerSupplier.get();
    }

    private Optional<BucketManagerSupplier> getSupplier() {
        return ServiceLoaderProvider
                .getSupplierStream(BucketManagerSupplier.class,
                        ()-> ServiceLoader.load(BucketManagerSupplier.class))
                .map(BucketManagerSupplier.class::cast)
                .findFirst();
    }

}
