/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
 * Exception thrown when a query function is not supported by the underlying NoSQL database.
 *
 * <p>Most NoSQL databases do not provide native support for scalar functions like
 * UPPER, LOWER, LEFT, RIGHT, ABS, and LENGTH. This exception is thrown when attempting
 * to use a function that the database provider cannot execute.</p>
 *
 * <p>Databases with known function support:</p>
 * <ul>
 *   <li>Couchbase (N1QL)</li>
 *   <li>Oracle NoSQL</li>
 *   <li>Neo4j (Cypher)</li>
 * </ul>
 *
 * <p>Example of handling:</p>
 * <pre>{@code
 * try {
 *     List<Word> words = template.select(Word.class)
 *         .where(Function.upper("term"))
 *         .eq("JAVA")
 *         .result();
 * } catch (UnsupportedFunctionException e) {
 *     // Fallback to client-side filtering or alternative approach
 *     logger.warn("Database does not support UPPER function: " + e.getMessage());
 * }
 * }</pre>
 *
 * @since 1.1.0
 * @see Function
 */
public class UnsupportedFunctionException extends UnsupportedOperationException {

    /**
     * Constructs a new exception with the specified function and database.
     *
     * @param functionName the name of the unsupported function
     * @param databaseName the name of the database
     */
    public UnsupportedFunctionException(String functionName, String databaseName) {
        super(String.format(
                "Function '%s' is not supported by %s. " +
                        "Consider using a database with SQL-compatible query support (e.g., Couchbase, Neo4j, Oracle NoSQL) " +
                        "or implement filtering at the application level.",
                functionName, databaseName
        ));
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public UnsupportedFunctionException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public UnsupportedFunctionException(String message, Throwable cause) {
        super(message, cause);
    }
}