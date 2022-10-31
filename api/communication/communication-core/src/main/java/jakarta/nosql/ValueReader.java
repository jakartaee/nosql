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
 * This class represents the reader on the {@link Value} instance.
 * When the Value needs a conversion, it will load the implementations from SPI, Java Service Provider.
 * The {@link Predicate} verifies if the reader has the support the class type.
 * @see Value
 * @see Value#get(Class)
 * @see ValueWriter
 */
public interface ValueReader extends Predicate<Class<?>> {

    /**
     * Converts the value to the class type target.
     *
     * @param type  - the new instance class
     * @param value - instance to be converted
     * @param <T>   - the type class
     * @return an instance converted in the proper type
     */
    <T> T read(Class<T> type, Object value);

}
