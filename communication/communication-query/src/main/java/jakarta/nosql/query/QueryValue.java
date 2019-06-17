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

package jakarta.nosql.query;

import java.util.function.Supplier;

/**
 * The value is the last element in a condition, and it defines what it 'll go to be used, with an operator, in a field target.
 *
 * @param <T> the type.
 */
public interface QueryValue<T> extends Supplier<T> {


    /**
     * Returns a value type
     *
     * @return a value type
     */
    ValueType getType();
}
