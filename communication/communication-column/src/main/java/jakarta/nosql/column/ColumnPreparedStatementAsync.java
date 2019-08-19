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

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Result;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * An object that represents a precompiled Query statement.
 */
public interface ColumnPreparedStatementAsync {


    /**
     * Binds an argument to a positional parameter.
     *
     * @param name     the parameter name
     * @param value    the parameter value
     * @return the same query instance
     * @throws NullPointerException     when there is null parameter
     */
    ColumnPreparedStatementAsync bind(String name, Object value);

    /**
     * Executes a query and return the result as List
     * @param callBack the callback
     */
    void getResultList(Consumer<Result<ColumnEntity>> callBack);

    /**
     * Returns the result as a single element otherwise it will return an {@link Optional#empty()}
     *
     * @param callBack the result callback
     * @throws NonUniqueResultException when the result has more than one entity
     */
    void getSingleResult(Consumer<Optional<ColumnEntity>> callBack);

}
