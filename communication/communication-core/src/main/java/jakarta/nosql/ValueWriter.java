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

/**
 * To put your own Java Structure in NoSQL database is necessary convert it to a supported one.
 * So, the ValueWriter has the goal to convert to any specific structure type that a database might support.
 * These implementation will loaded by ServiceLoad and a NoSQL implementation will may use it.
 *
 * @param <T> current type
 * @param <S> the converted type
 */
public interface ValueWriter<T, S> {

    /**
     * verifies if the writer has support of instance from this class.
     *
     * @param <C>   the class type
     * @param clazz - {@link Class} to be verified
     * @return true if the implementation is can support this class, otherwise false
     */
    <C> boolean isCompatible(Class<C> clazz);

    /**
     * Converts a specific structure to a new one.
     *
     * @param object the instance to be converted
     * @return a new instance with the new class
     */

    S write(T object);
}
