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

import java.util.Objects;

/**
 * {@code Function} represents a function expression that can be applied to entity fields in queries.
 * Function expressions allow scalar operations to be performed on fields within query conditions,
 * aligning with the Jakarta Query specification and providing type-safe function usage in the fluent API.
 *
 * <p>Jakarta NoSQL supports the following scalar functions:</p>
 * <p>String Functions:</p>
 * <ul>
 *   <li>{@link #left(String, int)} - Extracts the leftmost characters from a string field</li>
 *   <li>{@link #right(String, int)} - Extracts the rightmost characters from a string field</li>
 *   <li>{@link #upper(String)} - Converts a string field to uppercase</li>
 *   <li>{@link #lower(String)} - Converts a string field to lowercase</li>
 *   <li>{@link #length(String)} - Returns the length of a string field</li>
 * </ul>
 * <p>Numeric Functions:</p>
 * <ul>
 *   <li>{@link #abs(String)} - Returns the absolute value of a numeric field</li>
 * </ul>
 *
 * <p>Functions are created using static factory methods and can be used in {@code where} clauses
 * of the fluent query API. The returned function expressions are immutable and thread-safe.</p>
 *
 * <p>Example usage with string functions:</p>
 * <pre>{@code
 * @Inject
 * Template template;
 *
 * List<Word> words = template.select(Word.class)
 *         .where(Function.left("term", 2))
 *         .eq("Ja")
 *         .result();
 *
 * List<Word> coffeeWords = template.select(Word.class)
 *         .where(Function.upper("meaning"))
 *         .eq("COFFEE")
 *         .result();
 *
 * List<Word> longWords = template.select(Word.class)
 *         .where(Function.length("term"))
 *         .gt(5)
 *         .result();
 * }</pre>
 *
 * <p>Example usage with numeric functions:</p>
 * <pre>{@code
 * List<Product> products = template.select(Product.class)
 *         .where(Function.abs("price"))
 *         .gt(10)
 *         .result();
 * }</pre>
 *
 * <p>Functions can also be combined with logical operators:</p>
 * <pre>{@code
 * List<Word> results = template.select(Word.class)
 *         .where(Function.upper("language"))
 *         .eq("EN")
 *         .and(Function.length("term"))
 *         .gt(5)
 *         .result();
 * }</pre>
 *
 * <p><strong>Database Support:</strong></p>
 * <p>Function support varies significantly across NoSQL databases. Most NoSQL databases do <strong>not</strong>
 * natively support scalar functions. Function support is generally limited to databases with SQL-compatible
 * query layers or specific query languages:</p>
 * <ul>
 *   <li><strong>Supported:</strong> Couchbase (N1QL), Oracle NoSQL, Neo4j (Cypher)</li>
 *   <li><strong>Not Supported:</strong> MongoDB, Cassandra, ScyllaDB, Redis, DynamoDB, TinkerPop (Gremlin)</li>
 * </ul>
 *
 * <p>When a function is not supported by the underlying database, an {@link UnsupportedFunctionException}
 * will be thrown at query execution time. Applications should handle this exception gracefully and consider
 * implementing fallback logic using application-level filtering.</p>
 *
 * <p>Example of exception handling:</p>
 * <pre>{@code
 * try {
 *     List<Word> words = template.select(Word.class)
 *             .where(Function.upper("term"))
 *             .eq("JAVA")
 *             .result();
 * } catch (UnsupportedFunctionException e) {
 *     List<Word> allWords = template.select(Word.class).result();
 *     List<Word> filtered = allWords.stream()
 *             .filter(w -> w.getTerm().equalsIgnoreCase("JAVA"))
 *             .collect(Collectors.toList());
 * }
 * }</pre>
 *
 * @see QueryMapper
 * @see Template
 * @since 1.1.0
 */
public interface Function {

    /**
     * Returns the name of the function (e.g., {@code "LEFT"}, {@code "UPPER"}, {@code "ABS"}).
     *
     * @return the function name, never {@code null}
     */
    String name();

    /**
     * Returns the field name this function operates on.
     *
     * @return the field name, never {@code null}
     */
    String field();

    /**
     * Returns the arguments passed to this function.
     * For functions without arguments (e.g., {@code UPPER}, {@code LOWER}), this returns an empty array.
     *
     * @return an array of function arguments, never {@code null}
     */
    Object[] arguments();

    /**
     * Creates a {@code LEFT} function expression that extracts the leftmost characters from a string field.
     *
     * <p>The {@code LEFT} function is equivalent to SQL's {@code LEFT(field, length)} and extracts
     * the specified number of characters from the beginning of a string value.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Word> words = template.select(Word.class)
     *         .where(Function.left("term", 2))
     *         .eq("Ja")
     *         .result();
     * }</pre>
     *
     * @param field the name of the field to apply the function to
     * @param length the number of characters to extract from the left
     * @return a {@code LEFT} function expression
     * @throws NullPointerException if {@code field} is {@code null}
     * @throws IllegalArgumentException if {@code length} is negative
     */
    static Function left(String field, int length) {
        Objects.requireNonNull(field, "field is required");
        if (length < 0) {
            throw new IllegalArgumentException("length must be non-negative");
        }
        return new DefaultFunction("LEFT", field, length);
    }

    /**
     * Creates a {@code RIGHT} function expression that extracts the rightmost characters from a string field.
     *
     * <p>The {@code RIGHT} function is equivalent to SQL's {@code RIGHT(field, length)} and extracts
     * the specified number of characters from the end of a string value.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Word> words = template.select(Word.class)
     *         .where(Function.right("term", 2))
     *         .eq("va")
     *         .result();
     * }</pre>
     *
     * @param field the name of the field to apply the function to
     * @param length the number of characters to extract from the right
     * @return a {@code RIGHT} function expression
     * @throws NullPointerException if {@code field} is {@code null}
     * @throws IllegalArgumentException if {@code length} is negative
     */
    static Function right(String field, int length) {
        Objects.requireNonNull(field, "field is required");
        if (length < 0) {
            throw new IllegalArgumentException("length must be non-negative");
        }
        return new DefaultFunction("RIGHT", field, length);
    }

    /**
     * Creates an {@code UPPER} function expression that converts a string field to uppercase.
     *
     * <p>The {@code UPPER} function is equivalent to SQL's {@code UPPER(field)} and converts
     * all characters in the string value to uppercase for case-insensitive comparisons.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Word> words = template.select(Word.class)
     *         .where(Function.upper("meaning"))
     *         .eq("COFFEE")
     *         .result();
     * }</pre>
     *
     * @param field the name of the field to apply the function to
     * @return an {@code UPPER} function expression
     * @throws NullPointerException if {@code field} is {@code null}
     */
    static Function upper(String field) {
        Objects.requireNonNull(field, "field is required");
        return new DefaultFunction("UPPER", field);
    }

    /**
     * Creates a {@code LOWER} function expression that converts a string field to lowercase.
     *
     * <p>The {@code LOWER} function is equivalent to SQL's {@code LOWER(field)} and converts
     * all characters in the string value to lowercase for case-insensitive comparisons.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Word> words = template.select(Word.class)
     *         .where(Function.lower("term"))
     *         .eq("java")
     *         .result();
     * }</pre>
     *
     * @param field the name of the field to apply the function to
     * @return a {@code LOWER} function expression
     * @throws NullPointerException if {@code field} is {@code null}
     */
    static Function lower(String field) {
        Objects.requireNonNull(field, "field is required");
        return new DefaultFunction("LOWER", field);
    }

    /**
     * Creates a {@code LENGTH} function expression that returns the length of a string field.
     *
     * <p>The {@code LENGTH} function is equivalent to SQL's {@code LENGTH(field)} and returns
     * the number of characters in the string value.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Word> words = template.select(Word.class)
     *         .where(Function.length("term"))
     *         .gt(5)
     *         .result();
     * }</pre>
     *
     * @param field the name of the field to apply the function to
     * @return a {@code LENGTH} function expression
     * @throws NullPointerException if {@code field} is {@code null}
     */
    static Function length(String field) {
        Objects.requireNonNull(field, "field is required");
        return new DefaultFunction("LENGTH", field);
    }

    /**
     * Creates an {@code ABS} function expression that returns the absolute value of a numeric field.
     *
     * <p>The {@code ABS} function is equivalent to SQL's {@code ABS(field)} and returns
     * the absolute value of a numeric field, converting negative values to positive.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Product> products = template.select(Product.class)
     *         .where(Function.abs("price"))
     *         .gt(10)
     *         .result();
     * }</pre>
     *
     * @param field the name of the field to apply the function to
     * @return an {@code ABS} function expression
     * @throws NullPointerException if {@code field} is {@code null}
     */
    static Function abs(String field) {
        Objects.requireNonNull(field, "field is required");
        return new DefaultFunction("ABS", field);
    }

    /**
     * Default immutable implementation of {@link Function} using a Java record.
     * This implementation provides thread-safe, immutable function expressions
     * with automatic implementation of {@code equals}, {@code hashCode}, and {@code toString}.
     *
     * @param name the name of the function
     * @param field the field name the function operates on
     * @param arguments optional arguments for the function
     */
    record DefaultFunction(
            String name,
            String field,
            Object... arguments
    ) implements Function {

        /**
         * Compact constructor that validates the function expression and creates defensive copies.
         *
         * @throws NullPointerException if {@code name} or {@code field} is {@code null}
         */
        public DefaultFunction {
            Objects.requireNonNull(name, "name is required");
            Objects.requireNonNull(field, "field is required");
            arguments = arguments == null ? new Object[0] : arguments.clone();
        }

        @Override
        public Object[] arguments() {
            return arguments.clone();
        }

        @Override
        public String toString() {
            var sb = new StringBuilder(name).append('(').append(field);
            for (var arg : arguments) {
                sb.append(", ").append(arg);
            }
            return sb.append(')').toString();
        }
    }
}