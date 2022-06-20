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

package jakarta.nosql.keyvalue;


import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.TypeSupplier;
import jakarta.nosql.Value;

import java.util.ServiceLoader;
import java.util.function.BiFunction;


/**
 * A bucket unit, it's a tuple that contains key its respective value.
 */
public interface KeyValueEntity {


    /**
     * Creates a Key value instance
     *
     * @param key   the key
     * @param value the value
     * @param <K>   the key type
     * @param <V>   the value type
     * @return a {@link KeyValueEntity} instance
     * @throws NullPointerException when either key or value are null
     */
    static <K, V> KeyValueEntity of(K key, V value) {
        return ServiceLoaderProvider.get(KeyValueEntityProvider.class,
                ()-> ServiceLoader.load(KeyValueEntityProvider.class)).apply(key, value);
    }

    /**
     * the key
     *
     * @return the value
     */
    Object getKey();


    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param clazz {@link Value#get(Class)}
     * @param <K>   {@link Value#get(Class)}
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    <K> K getKey(Class<K> clazz);

    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param typeSupplier {@link Value#get(TypeSupplier)}
     * @param <K>          {@link Value#get(TypeSupplier)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link Value#get(TypeSupplier)}
     */
    <K> K getKey(TypeSupplier<K> typeSupplier);

    /**
     * The value
     *
     * @return the value
     * @see Value
     */
    Object getValue();

    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param clazz {@link Value#get(Class)}
     * @param <V>   {@link Value#get(Class)}
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    <V> V getValue(Class<V> clazz);

    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param typeSupplier {@link Value#get(TypeSupplier)}
     * @param <V>          {@link Value#get(TypeSupplier)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link Value#get(TypeSupplier)}
     */
    <V> V getValue(TypeSupplier<V> typeSupplier);

    /**
     * A provider of {@link KeyValueEntity} where it will return from two values where the first one is the key
     * and the second one is the value of the entity.
     */
    interface KeyValueEntityProvider extends BiFunction<Object, Object, KeyValueEntity> {
    }
}
