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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A unit that has the columnFamily and condition to delete from conditions
 * <p>
 * This instance will be used on:
 * <p>{@link DocumentCollectionManager#delete(DocumentDeleteQuery)}</p>
 */
public interface DocumentDeleteQuery {

    /**
     * getter the collection name
     *
     * @return the collection name
     */
    String getDocumentCollection();

    /**
     * getter the condition
     *
     * @return the condition
     */
    Optional<DocumentCondition> getCondition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link DocumentDeleteQuery}
     *
     * @return the columns
     */
    List<String> getDocuments();

    /**
     * It starts the first step of {@link DocumentDelete} API using a fluent-API way.
     * This first step will inform the fields to delete in the query instead of the whole record.
     * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
     *
     * @param documents - The column fields to query, optional.
     * @return a new {@link DocumentDelete} instance
     * @throws NullPointerException when there is a null element
     */
    static DocumentDelete delete(String... documents) {
        return ServiceLoaderProvider.get(DocumentDeleteProvider.class).apply(documents);
    }

    /**
     * It starts the first step of {@link DocumentDelete} API using a fluent-API way.
     * Once there is no field, it will remove the whole record instead of some fields on the database.
     *
     * @return a new {@link DocumentDelete} instance
     * @throws NullPointerException when there is a null element
     */
    static DocumentDelete delete() {
        return ServiceLoaderProvider.get(DocumentDeleteProvider.class).get();
    }

    /**
     * The initial element in the Document delete query
     */
    interface DocumentDelete {

        /**
         * Defines the document collection in the delete query
         *
         * @param documentCollection the document collection to query
         * @return a {@link DocumentDeleteFrom query}
         * @throws NullPointerException when documentCollection is null
         */
        DocumentDeleteFrom from(String documentCollection);

    }

    /**
     * A provider class of {@link DocumentDelete}
     */
    interface DocumentDeleteProvider extends Function<String[], DocumentDelete>, Supplier<DocumentDelete> {
    }


    /**
     * The Document Delete Query
     */
    interface DocumentDeleteFrom extends DocumentDeleteQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link DocumentDeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        DocumentDeleteNameCondition where(String name);

    }

    /**
     * The base to delete name condition
     */
    interface DocumentDeleteNameCondition {

        /**
         * Creates the equals condition {@link Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere eq(T value);

        /**
         * Creates the like condition {@link Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        DocumentDeleteWhere like(String value);

        /**
         * Creates the greater than condition {@link Condition#GREATER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere gte(T value);

        /**
         * Creates the lesser than condition {@link Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere lte(T value);

        /**
         * Creates the between condition {@link Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> DocumentDeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link Condition#NOT}
         *
         * @return {@link DocumentDeleteNotCondition}
         */
        DocumentDeleteNotCondition not();

    }

    /**
     * The document not condition
     */
    interface DocumentDeleteNotCondition extends DocumentDeleteNameCondition {
    }

    /**
     * The last step to the build of {@link DocumentDeleteQuery}.
     * It either can return a new {@link DocumentDeleteQuery} instance or execute a query with
     * {@link DocumentCollectionManager}
     */
    interface DocumentDeleteQueryBuild {

        /**
         * Creates a new instance of {@link DocumentDeleteQuery}
         *
         * @return a new {@link DocumentDeleteQuery} instance
         */
        DocumentDeleteQuery build();

        /**
         * executes the {@link DocumentCollectionManager#delete(DocumentDeleteQuery)}
         *
         * @param manager the entity manager
         * @throws NullPointerException when manager is null
         */
        void delete(DocumentCollectionManager manager);

    }

    /**
     * The Document Where whose define the condition in the delete query.
     */
    interface DocumentDeleteWhere extends DocumentDeleteQueryBuild {


        /**
         * Starts a new condition in the select using {@link DocumentCondition#and(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentDeleteNameCondition and(String name);

        /**
         * Starts a new condition in the select using {@link DocumentCondition#or(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentDeleteNameCondition or(String name);

    }
}
