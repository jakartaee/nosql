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

package jakarta.nosql.document;

import jakarta.nosql.QueryException;
import jakarta.nosql.Result;

import java.util.Optional;

/**
 * A query parser to document database type, this class will convert a String to an operation in {@link DocumentCollectionManager}.
 */
public interface DocumentQueryParser {

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param collectionManager the collection manager
     * @param observer          the observer
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    Result<DocumentEntity> query(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer);

    /**
     * Executes a query and returns as a single result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an {@link Optional#empty()}.
     *
     * @param query the query as {@link String}
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws IllegalStateException    when there is not {@link DocumentQueryParser}
     * @throws QueryException           when there is error in the syntax
     * @throws jakarta.nosql.NonUniqueResultException when the result has more than one entity
     */
    Optional<DocumentEntity> singleResult(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer);

    /**
     * Executes a query and returns a {@link DocumentPreparedStatement}, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query             the query as {@link String}
     * @param collectionManager the collection manager
     * @param observer          the observer
     * @return a {@link DocumentPreparedStatement} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws QueryException when there is error in the syntax
     */
    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer);


}
