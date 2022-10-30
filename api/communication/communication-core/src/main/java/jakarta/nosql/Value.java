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
package jakarta.nosql;


import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * It represents a value that is to/from a database.
 * Where to read it operates the {@link ValueReader} and writes it using {@link ValueWriter}.
 * For both reading and writing, it will load those implementations from SPI.
 *
 * @see ValueReader
 * @see ValueWriter
 */
public interface Value {

    /**
     * Returns the value without conversion.
     *
     * @return the instance inside {@link Value}
     */
    Object get();

    /**
     * Converts {@link Value#get()} to specified class
     *
     * @param type the class type
     * @param <T>  the new instance type
     * @return a new instance converted to informed class
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(Class<T> type);

    /**
     * Converts {@link Value#get()} to specified class
     *
     * @param supplier the type supplier
     * @param <T>      the new instance type
     * @return a new instance converted to informed class
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(TypeSupplier<T> supplier);

    /**
     * A wrapper of {@link Class#isInstance(Object)} to check the value instance within the {@link Value}
     *
     * @param type the type
     * @return {@link Class#isInstance(Object)}
     * @throws NullPointerException when type is null
     */
    boolean isInstanceOf(Class<?> type);


    /**
     * Creates a new {@link Value} instance
     *
     * @param value - the information to {@link Value}
     * @return a {@link Value} instance within a value informed
     * @throws NullPointerException when the parameter is null
     */
    static Value of(Object value) {
        return ServiceLoaderProvider.get(ValueProvider.class,
                () -> ServiceLoader.load(ValueProvider.class)).apply(value);
    }

    /**
     * A provider that creates a {@link Value} instance from an object
     */
    interface ValueProvider extends Function<Object, Value> {
    }
}
