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
import jakarta.nosql.Sort;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Class that contains information to do a select to {@link DocumentCollectionManager}
 *
 * @see DocumentCollectionManager#select(DocumentQuery)
 * @see DocumentCondition
 * @see Sort
 */
public interface DocumentQuery {


    /**
     * @return The maximum number of results the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getLimit();

    /**
     * @return The position of the first result the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getSkip();


    /**
     * The document collection name
     *
     * @return the document collection name
     */
    String getDocumentCollection();

    /**
     * The conditions that contains in this {@link DocumentQuery}
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or returns same elements in the database.
     *
     * @return the conditions
     */
    Optional<DocumentCondition> getCondition();

    /**
     * The sorts that contains in this {@link DocumentQuery}
     * The implementation might ignore this option.
     *
     * @return the sorts
     */
    List<Sort> getSorts();

    /**
     * Returns the documents to returns in that query if empty will return all elements in the query.
     * The implementation might ignore this option.
     *
     * @return the documents
     */
    List<String> getDocuments();

    /**
     * Creates a query to Document
     *
     * @param documents - The document fields to query, optional.
     * @return a new {@link DocumentSelect} instance
     * @throws NullPointerException when there is a null element
     */
    static DocumentSelect select(String... documents) {
        return ServiceLoaderProvider.get(DocumentSelectProvider.class).apply(documents);
    }

    /**
     * Creates a query to Document
     *
     * @return a new {@link DocumentSelect} instance
     * @throws NullPointerException when there is a null element
     */
    static DocumentSelect select() {
        return ServiceLoaderProvider.get(DocumentSelectProvider.class).get();
    }

    /**
     * The DocumentFrom Query
     */
    interface DocumentFrom extends DocumentQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link DocumentNameCondition}
         * @throws NullPointerException when name is null
         */
        DocumentNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrive
         * @return a query with first result defined
         */
        DocumentSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentLimit limit(long limit);

        /**
         * Add the order how the result will returned
         *
         * @param name the order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentOrder orderBy(String name);


    }

    /**
     * A provider class of {@link DocumentSelect}
     */
    interface DocumentSelectProvider extends Function<String[], DocumentSelect>, Supplier<DocumentSelect> {
    }



    /**
     * The Document Order whose define the the maximum number of results to retrieve.
     */
    interface DocumentLimit extends DocumentQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrive
         * @return a query with first result defined
         */
        DocumentSkip skip(long skip);

    }

    /**
     * The base to name condition
     */
    interface DocumentNameCondition {


        /**
         * Creates the equals condition {@link Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere eq(T value);

        /**
         * Creates the like condition {@link Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        DocumentWhere like(String value);

        /**
         * Creates the greater than condition {@link Condition#GREATER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere gte(T value);

        /**
         * Creates the lesser than condition {@link Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere lte(T value);

        /**
         * Creates the between condition {@link Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> DocumentWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link Condition#NOT}
         *
         * @return {@link DocumentNotCondition}
         */
        DocumentNotCondition not();
    }

    /**
     * The Column name order to the builder
     */
    interface DocumentNameOrder extends DocumentQueryBuild {

        /**
         * Add the order how the result will returned
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentOrder orderBy(String name);


        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrive
         * @return a query with first result defined
         */
        DocumentSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentLimit limit(long limit);



    }

    /**
     * The column not condition
     */
    interface DocumentNotCondition extends DocumentNameCondition {
    }

    /**
     * The initial element in the Document query
     */
    interface DocumentSelect {

        /**
         * Defines the document collection in the query
         *
         * @param documentCollection the document collection to query
         * @return a {@link DocumentFrom query}
         * @throws NullPointerException when documentCollection is null
         */
        DocumentFrom from(String documentCollection);
    }

    /**
     * The Document Order whose define the position of the first result to retrieve.
     */
    interface DocumentSkip extends DocumentQueryBuild {

        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentLimit limit(long limit);

    }

    /**
     * The Document Where whose define the condition in the query.
     */
    interface DocumentWhere extends DocumentQueryBuild {


        /**
         * Starts a new condition in the select using {@link DocumentCondition#and(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentNameCondition and(String name);

        /**
         * Appends a new condition in the select using {@link DocumentCondition#or(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrive
         * @return a query with first result defined
         */
        DocumentSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentLimit limit(long limit);

        /**
         * Add the order how the result will returned
         *
         * @param name the order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentOrder orderBy(String name);

    }

    /**
     * The Document Order whose define the sort in the query.
     */
    interface DocumentOrder {

        /**
         * Defines the order as {@link jakarta.nosql.SortType#ASC}
         * @return the {@link DocumentNameOrder} instance
         */
        DocumentNameOrder asc();

        /**
         * Defines the order as {@link jakarta.nosql.SortType#DESC}
         * @return the {@link DocumentNameOrder} instance
         */
        DocumentNameOrder desc();
    }

    /**
     * The last step to the build of {@link DocumentQuery}.
     * It either can return a new {@link DocumentQuery} instance or execute a query with
     * {@link DocumentCollectionManager}
     * and {@link DocumentCollectionManagerAsync}
     */
    interface DocumentQueryBuild {

        /**
         * Creates a new instance of {@link DocumentQuery}
         *
         * @return a new {@link DocumentQuery} instance
         */
        DocumentQuery build();

        /**
         * Executes {@link DocumentCollectionManager#select(DocumentQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link DocumentCollectionManager#select(DocumentQuery)}
         * @throws NullPointerException when manager is null
         */
        Stream<DocumentEntity> getResult(DocumentCollectionManager manager);

        /**
         * Executes {@link DocumentCollectionManager#singleResult(DocumentQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link DocumentCollectionManager#singleResult(DocumentQuery)}
         * @throws NullPointerException when manager is null
         */
        Optional<DocumentEntity> getSingleResult(DocumentCollectionManager manager);

        /**
         * Executes {@link DocumentCollectionManagerAsync#select(DocumentQuery, Consumer)}
         *
         * @param manager  the entity manager
         * @param callback the callback
         * @throws NullPointerException when there is null parameter
         */
        void getResult(DocumentCollectionManagerAsync manager, Consumer<Stream<DocumentEntity>> callback);

        /**
         * Executes {@link DocumentCollectionManagerAsync#singleResult(DocumentQuery, Consumer)}
         *
         * @param manager  the entity manager
         * @param callback the callback
         * @throws NullPointerException when there is null parameter
         */
        void getSingleResult(DocumentCollectionManagerAsync manager, Consumer<Optional<DocumentEntity>> callback);
    }
}
