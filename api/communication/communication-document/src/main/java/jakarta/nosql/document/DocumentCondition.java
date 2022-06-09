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


import jakarta.nosql.Condition;
import jakarta.nosql.ServiceLoaderProvider;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * An unit condition  to run a document collection select
 *
 * @see DocumentCollectionManager#select(DocumentQuery)
 */
public interface DocumentCondition {

    /**
     * Gets the document to be used in the select
     *
     * @return a document instance
     */
    Document getDocument();

    /**
     * Gets the conditions to be used in the select
     *
     * @return a Condition instance
     * @see Condition
     */
    Condition getCondition();

    /**
     * Creates a new {@link DocumentCondition} using the {@link Condition#AND}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    DocumentCondition and(DocumentCondition condition);

    /**
     * Creates a new {@link DocumentCondition} negating the current one
     *
     * @return the negated condition
     * @see Condition#NOT
     */
    DocumentCondition negate();

    /**
     * Creates a new {@link DocumentCondition} using the {@link Condition#OR}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    DocumentCondition or(DocumentCondition condition);

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#EQUALS}, it means a select will scanning to a
     * document collection that has the same name and equals value informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition eq(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(document, Condition.EQUALS);
    }

    /**
     * an alias method to {@link DocumentCondition#eq(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition eq(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class)
                .apply(Document.of(name, value), Condition.EQUALS);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#GREATER_THAN},
     * it means a select will scanning to a document collection that has the same name and the value
     * greater than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition gt(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(document, Condition.GREATER_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#gt(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition gt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(Document.of(name, value)
                , Condition.GREATER_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#GREATER_EQUALS_THAN},
     * it means a select will scanning to a document collection that has the same name and the value
     * greater or equals than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition gte(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(document, Condition.GREATER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#gte(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition gte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(Document.of(name, value)
                , Condition.GREATER_EQUALS_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LESSER_THAN}, it means a select will scanning to a
     * document collection that has the same name and the value  lesser than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition lt(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(document, Condition.LESSER_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#lt(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition lt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(Document.of(name, value)
                , Condition.LESSER_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LESSER_EQUALS_THAN},
     * it means a select will scanning to a document collection that has the same name and the value
     * lesser or equals than informed in this document.
     *
     * @param document a document instance
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition lte(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(document, Condition.LESSER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#lte(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition lte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(Document.of(name, value)
                , Condition.LESSER_EQUALS_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#IN}, it means a select will scanning to a
     * document collection that has the same name and the value is within informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#IN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition in(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).in(document);
    }

    /**
     * an alias method to {@link DocumentCondition#in(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#IN}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition in(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).in(Document.of(name, value));
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LIKE}, it means a select will scanning to a
     * document collection that has the same name and the value  is like than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition like(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(document, Condition.LIKE);
    }


    /**
     * an alias method to {@link DocumentCondition#like(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition like(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).apply(Document.of(name, value)
                , Condition.LIKE);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#BETWEEN},
     * it means a select will scanning to a document collection that is between two values informed
     * on a document name.
     * The document must have a {@link Document#get()} an {@link Iterable} implementation
     * with just two elements.
     *
     * @param document a column instance
     * @return The between condition
     * @throws NullPointerException     when document is null
     * @throws IllegalArgumentException When the document neither has an Iterable instance or two elements on
     *                                  an Iterable.
     */
    static DocumentCondition between(Document document) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).between(document);
    }

    /**
     * Returns a predicate that is the negation of the supplied predicate.
     * This is accomplished by returning result of the calling target.negate().
     *
     * @param condition
     * @return a condition that negates the results of the supplied predicate
     * @throws NullPointerException when condition is null
     */
    static DocumentCondition not(DocumentCondition condition) {
        Objects.requireNonNull(condition, "condition is required");
        return condition.negate();
    }

    /**
     * an alias method to {@link DocumentCondition#between(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     * @param name the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#BETWEEN}
     * @throws NullPointerException when either name or value is null
     */
    static DocumentCondition between(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(DocumentConditionProvider.class)
                .between(Document.of(name, value));
    }

    /**
     * Returns a new {@link DocumentCondition} aggregating ,as "AND", all the conditions as just one condition.
     * The {@link Document} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * <pre>
     * {@code
     * Document age = Document.of("age", 26);
     * Document name = Document.of("name", "otavio");
     * DocumentCondition condition = DocumentCondition.eq(name).and(DocumentCondition.gte(age));
     * }
     * </pre>
     * The {@link DocumentCondition#getDocument()} will have "_AND" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link DocumentCondition} instance
     * @throws NullPointerException when the conditions is null
     */
    static DocumentCondition and(DocumentCondition... conditions) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).and(conditions);
    }

    /**
     * Returns a new {@link DocumentCondition} aggregating ,as "OR", all the conditions as just one condition.
     * The {@link Document} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * <pre>
     * {@code
     * Document age = Document.of("age", 26);
     * Document name = Document.of("name", "otavio");
     * ColumnCondition condition = DocumentCondition.eq(name).or(DocumentCondition.gte(age));
     * }
     * </pre>
     * The {@link DocumentCondition#getDocument()} will have "_OR" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link DocumentCondition} instance
     * @throws NullPointerException when the condition is null
     */
    static DocumentCondition or(DocumentCondition... conditions) {
        return ServiceLoaderProvider.get(DocumentConditionProvider.class).or(conditions);
    }

    /**
     * A provider of {@link DocumentCondition} where it will create from two parameters:
     * The first one is {@link Document}
     * The second one is the Condition
     */
    interface DocumentConditionProvider extends BiFunction<Document, Condition, DocumentCondition> {

        /**
         * Creates a {@link Condition#BETWEEN} operation
         * @param document the document
         * @return a {@link DocumentCondition}
         */
        DocumentCondition between(Document document);

        /**
         * Creates a {@link Condition#AND} operation
         * @param conditions the conditions
         * @return a {@link DocumentCondition}
         */
        DocumentCondition and(DocumentCondition... conditions);

        /**
         * Creates a {@link Condition#OR} operation
         * @param conditions the conditions
         * @return a {@link DocumentCondition}
         */
        DocumentCondition or(DocumentCondition... conditions);

        /**
         * Creates a {@link Condition#IN} operation
         * @param document the document
         * @return a {@link DocumentCondition}
         */
        DocumentCondition in(Document document);
    }
}
