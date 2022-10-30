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

import java.util.function.Predicate;

/**
 * The reader to {@link TypeReference}
 * The {@link Predicate} verifies if the TypeReferenceReader has the support of instance from this class.
 *
 * @see Value#get(TypeSupplier)
 */
public interface TypeReferenceReader extends Predicate<TypeSupplier<?>> {

    /**
     * converts to defined type on {@link TypeReference}
     *
     * @param typeReference the typeReference
     * @param value         the value
     * @param <T>           the typeReference type
     * @return the instance converted
     */
    <T> T convert(TypeSupplier<T> typeReference, Object value);

}
