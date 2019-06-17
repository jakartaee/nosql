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
package jakarta.nosql.key;

import jakarta.nosql.QueryException;
import jakarta.nosql.Value;

import java.util.List;

/**
 * A query parser to key-value database type, this class will convert a String to an operation in {@link BucketManager}.
 */
public interface KeyValueQueryParser {

    /**
     * Executes a query and returns the result, when the operations are <b>put</b>, <b>get</b> and <b>del</b>
     * command it will return the result of the operation when the command is either <b>put</b> or <b>del</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param manager the manager
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    List<Value> query(String query, BucketManager manager);

    /**
     * Executes a query and returns a {@link KeyValuePreparedStatement}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>del</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param manager the manager
     * @return a {@link KeyValuePreparedStatement} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    KeyValuePreparedStatement prepare(String query, BucketManager manager);

}
