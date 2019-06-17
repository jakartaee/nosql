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

import jakarta.nosql.QueryException;

import java.util.List;
import java.util.function.Consumer;

/**
 * A query parser to column database type, this class will convert a String to an operation in {@link ColumnFamilyManagerAsync}.
 */
public interface ColumnQueryParserAsync {

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query    the query as {@link String}
     * @param manager  the manager
     * @param observer the observer
     * @param callBack the callback
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    void query(String query, ColumnFamilyManagerAsync manager, Consumer<List<ColumnEntity>> callBack, ColumnObserverParser observer);

    /**
     * Executes a query and returns a {@link ColumnPreparedStatementAsync}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query    the query as {@link String}
     * @param observer the observer
     * @param manager  the manager
     * @return a {@link ColumnPreparedStatementAsync} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    ColumnPreparedStatementAsync prepare(String query, ColumnFamilyManagerAsync manager, ColumnObserverParser observer);


}
