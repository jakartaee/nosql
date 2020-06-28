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


import java.util.function.Predicate;

/**
 * This interface represents the converters to be used in Value method,
 * so if there's a new type that the current API doesn't support just creates a new implementation and
 * load it by service load process.
 * The {@link Predicate} verifies if the reader has the support of instance from this class.
 * @see Value
 * @see Value#get(Class)
 */
public interface ValueReader extends Predicate<Class<?>> {

    /**
     * Once this implementation is compatible with the class type, the next step it converts  an
     * instance to this new one from the rightful class.
     *
     * @param clazz - the new instance class
     * @param value - instance to be converted
     * @param <T>   - the new type class
     * @return a new instance converted from required class
     */
    <T> T read(Class<T> clazz, Object value);

}
