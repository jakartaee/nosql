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


import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.TypeSupplier;
import jakarta.nosql.Value;

import java.util.function.BiFunction;

/**
 * A Column Family entity unit, it is a tuple (pair) that consists of a key-value pair,
 * where the key is mapped to a value.
 */
public interface Column {


    /**
     * Creates a column instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @param <V>   the value type
     * @return a column instance
     * @throws NullPointerException when there is any null parameter
     * @see Columns
     */
    static <V> Column of(String name, V value) {
        return ServiceLoaderProvider.get(ColumnProvider.class).apply(name, value);
    }


    /**
     * The column's name
     *
     * @return name
     */
    String getName();

    /**
     * the column's value
     *
     * @return {@link Value}
     */
    Value getValue();

    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param clazz the clazz
     * @param <T>   the type
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    <T> T get(Class<T> clazz);

    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param typeSupplier {@link Value#get(Class)}
     * @param <T>          {@link Value#get(Class)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    <T> T get(TypeSupplier<T> typeSupplier);

    /**
     * Alias to {@link Value#get()}
     *
     * @return {@link Value#get()}
     */
    Object get();

    /**
     * A provider of {@link Column} where it will create from two parameters:
     * The first one is the name of column
     * The second one is the information of column
     */
    interface ColumnProvider extends BiFunction<String, Object, Column> {
    }
}
