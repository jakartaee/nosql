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
import java.util.stream.StreamSupport;

public interface Result<T> extends Iterable<T> {


    default Stream<T> toStream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    static <E, T> Result<T> of(Iterable<E> entities, Function<E, T> converter) {
        Objects.requireNonNull(converter, "converter is required");
        Objects.requireNonNull(entities, "entities is required");
        final ResultSupplier<T, E> supplier = ServiceLoaderProvider.get(ResultSupplier.class);
        return supplier.apply(converter, entities);
    }

    interface ResultSupplier<T, E> extends BiFunction<Function<E, T>, Iterable<E>, Result<T>> {

    }
}
