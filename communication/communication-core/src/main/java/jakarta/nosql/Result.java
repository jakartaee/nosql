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

package jakarta.nosql;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A pointer to the result set of a query. Vendors might implement lazily fetching from the database.
 * <p>
 * The implementation method might return a {@link java.util.Iterator} where the method
 * {@link java.util.Iterator#remove} returns a {@link UnsupportedOperationException}
 * once it is to read-only operation.
 * </p>
 *
 * @param <T> the element type
 */
public interface Result<T> extends Iterable<T> {


    /**
     * Returns a sequential {@link Stream} with this result as its source.
     *
     * @return a {@link Stream} from the result
     */
    Stream<T> stream();

    /**
     * Creates a new instance from the entity database and the converters
     *
     * @param entities  the entities source
     * @param converter the converter that translate the origin entity to the target entity
     * @param <E>       the origin source entity type
     * @param <T>       the entity destination entity type
     * @return a {@link Result} instance
     * @throws NullPointerException      when there is null parameter
     * @throws ProviderNotFoundException when the provider is not found
     */
    static <E, T> Result<T> of(Iterable<E> entities, Function<E, T> converter) {
        Objects.requireNonNull(converter, "converter is required");
        Objects.requireNonNull(entities, "entities is required");
        final ResultSupplier<T, E> supplier = ServiceLoaderProvider.get(ResultSupplier.class);
        return supplier.apply(entities, converter);
    }

    /**
     * It returns a Supplier of {@link Result} where given a {@link Function} converter and the source iterable
     * it will create a {@link Result} instance
     *
     * @param <E> the origin source type
     * @param <T> the entity destination type
     */
    interface ResultSupplier<T, E> extends BiFunction<Iterable<E>, Function<E, T>, Result<T>> {

    }
}
