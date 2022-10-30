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
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This class represents the writer on the {@link Value} instance.
 * Before sending the information to the NoSQL database, it will load the implementations from SPI,
 * Java Service Provider, and write to the proper format.
 * The {@link Predicate} verifies if the writer has the support of instance from this class.
 *
 * @param <T> current type
 * @param <S> the converted type
 */
public interface ValueWriter<T, S> extends Predicate<Class<?>> {

    /**
     * Converts a specific structure to a new one.
     *
     * @param object the instance to be converted
     * @return a new instance with the new class
     */
    S write(T object);

    /**
     * Returns the {@link Stream} of all {@link ValueWriter} available
     *
     * @param <T> current type
     * @param <S> the converted type
     * @return the stream of writers
     */
    static <T, S> Stream<ValueWriter<T, S>> getWriters() {
        return ServiceLoaderProvider.getSupplierStream(ValueWriter.class,
                        () -> ServiceLoader.load(ValueWriter.class))
                .map(ValueWriter.class::cast);
    }
}
