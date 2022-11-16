/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package jakarta.nosql.mapping.document;


import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The builder to either select and delete query using an object mapper API.
 */
public interface DocumentQueryMapper {


    /**
     * Returns a {@link DocumentMapperFrom} implementation that does the object mapper API.
     *
     * @param type the entity class
     * @param <T>         the entity type
     * @return a {@link DocumentMapperFrom} instance
     * @throws NullPointerException when type is null
     */
    <T> DocumentMapperFrom selectFrom(Class<T> type);

    /**
     * Returns a {@link DocumentMapperDeleteFrom} implementation that does the object mapper API.
     *
     * @param type the entity class
     * @param <T>         the entity type
     * @return a {@link DocumentMapperDeleteFrom} instance
     * @throws NullPointerException when type is null
     */
    <T> DocumentMapperDeleteFrom deleteFrom(Class<T> type);

    /**
     * The Document Delete Query
     */
    interface DocumentMapperDeleteFrom extends DocumentMapperDeleteQueryBuild {
        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link DocumentMapperDeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        DocumentMapperDeleteNameCondition where(String name);
    }

    /**
     * The base to delete name condition
     */
    interface DocumentMapperDeleteNameCondition {

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperDeleteWhere eq(T value);

        /**
         * Creates the like condition {@link jakarta.nosql.Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        DocumentMapperDeleteWhere like(String value);

        /**
         * Creates the greater than condition {@link jakarta.nosql.Condition#GREATER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperDeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link jakarta.nosql.Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperDeleteWhere gte(T value);

        /**
         * Creates the lesser than condition {@link jakarta.nosql.Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperDeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link jakarta.nosql.Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperDeleteWhere lte(T value);

        /**
         * Creates the between condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> DocumentMapperDeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link jakarta.nosql.Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link DocumentMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperDeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#NOT}
         *
         * @return {@link DocumentMapperDeleteNotCondition}
         */
        DocumentMapperDeleteNotCondition not();
    }

    /**
     * The document not condition
     */
    interface DocumentMapperDeleteNotCondition extends DocumentMapperDeleteNameCondition {
    }

    /**
     * The last step to the build of {@link DocumentDeleteQuery}.
     * It either can return a new {@link DocumentDeleteQuery} instance or execute a query with
     * {@link DocumentTemplate}
     */
    interface DocumentMapperDeleteQueryBuild {
        /**
         * Creates a new instance of {@link DocumentDeleteQuery}
         *
         * @return a new {@link DocumentDeleteQuery} instance
         */
        DocumentDeleteQuery build();

        /**
         * executes the {@link DocumentTemplate#delete(DocumentDeleteQuery)}
         *
         * @param template the document template
         * @throws NullPointerException when manager is null
         */
        void delete(DocumentTemplate template);
    }

    /**
     * The Document Where whose define the condition in the delete query.
     */
    interface DocumentMapperDeleteWhere extends DocumentMapperDeleteQueryBuild {

        /**
         * Starts a new condition in the select using {@link jakarta.nosql.document.DocumentCondition#and(jakarta.nosql.document.DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentMapperDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentMapperDeleteNameCondition and(String name);

        /**
         * Starts a new condition in the select using {@link jakarta.nosql.document.DocumentCondition#or(jakarta.nosql.document.DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentMapperDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentMapperDeleteNameCondition or(String name);
    }

    /**
     * The DocumentFrom Query
     */
    interface DocumentMapperFrom extends DocumentMapperQueryBuild {

        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link DocumentMapperNameCondition}
         * @throws NullPointerException when name is null
         */
        DocumentMapperNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        DocumentMapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentMapperLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentMapperOrder orderBy(String name);
    }

    /**
     * The Document Order whose define the maximum number of results to retrieve.
     */
    interface DocumentMapperLimit extends DocumentMapperQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        DocumentMapperSkip skip(long skip);
    }

    /**
     * The base to name condition
     */
    interface DocumentMapperNameCondition {

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperWhere eq(T value);

        /**
         * Creates the like condition {@link jakarta.nosql.Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when value is null
         */
        DocumentMapperWhere like(String value);

        /**
         * Creates the greater than condition {@link jakarta.nosql.Condition#GREATER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link jakarta.nosql.Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperWhere gte(T value);

        /**
         * Creates the lesser than condition {@link jakarta.nosql.Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link jakarta.nosql.Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperWhere lte(T value);

        /**
         * Creates the between condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> DocumentMapperWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link jakarta.nosql.Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link DocumentMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentMapperWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#NOT}
         *
         * @return {@link DocumentMapperNotCondition}
         */
        DocumentMapperNotCondition not();
    }

    /**
     * The Column name order to the builder
     */
    interface DocumentMapperNameOrder extends DocumentMapperQueryBuild {

        /**
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentMapperOrder orderBy(String name);


        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        DocumentMapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentMapperLimit limit(long limit);
    }

    /**
     * The column not condition
     */
    interface DocumentMapperNotCondition extends DocumentMapperNameCondition {
    }

    /**
     * The Document Order whose define the sort in the query.
     */
    interface DocumentMapperOrder {

        /**
         * Defines the order as {@link jakarta.nosql.SortType#ASC}
         * @return the {@link DocumentMapperNameOrder} instance
         */
        DocumentMapperNameOrder asc();

        /**
         * Defines the order as {@link jakarta.nosql.SortType#DESC}
         * @return the {@link DocumentMapperNameOrder} instance
         */
        DocumentMapperNameOrder desc();
    }

    /**
     * The last step to the build of {@link jakarta.nosql.document.DocumentQuery}.
     * It either can return a new {@link jakarta.nosql.document.DocumentQuery} instance or execute a query with
     * {@link DocumentTemplate}
     */
    interface DocumentMapperQueryBuild {

        /**
         * Creates a new instance of {@link DocumentQuery}
         *
         * @return a new {@link DocumentQuery} instance
         */
        DocumentQuery build();

        /**
         * Creates a new instance of {@link DocumentQuery} from {@link Pagination}
         *
         * @param pagination the pagination
         * @return a new {@link DocumentQuery} instance from {@link Pagination}
         */
        DocumentQuery build(Pagination pagination);

        /**
         * Executes {@link DocumentTemplate#select(DocumentQuery)}
         *
         * @param <T>      the entity type
         * @param template the template to document
         * @return the result of {@link DocumentTemplate#select(DocumentQuery)}
         * @throws NullPointerException when manager is null
         */
        <T> Stream<T> getResult(DocumentTemplate template);

        /**
         * Executes {@link DocumentTemplate#singleResult(DocumentQuery)}
         *
         * @param <T>      the entity type
         * @param template the template to document
         * @return the result of {@link DocumentTemplate#singleResult(DocumentQuery)}
         * @throws NullPointerException when manager is null
         */
        <T> Optional<T> getSingleResult(DocumentTemplate template);

        /**
         * Executes {@link DocumentTemplate#select(DocumentQuery)} using {@link Pagination}
         *
         * @param <T>        the entity type
         * @param template   the document template
         * @param pagination the pagination
         * @return the result of {@link DocumentTemplate#select(DocumentQuery)}
         * @throws NullPointerException when there are null parameters
         */
        <T> Stream<T> getResult(DocumentTemplate template, Pagination pagination);

        /**
         * Executes {@link DocumentTemplate#singleResult(DocumentQuery)} using {@link Pagination}
         *
         * @param <T>        the entity type
         * @param template   the document template
         * @param pagination the pagination
         * @return the result of {@link DocumentTemplate#singleResult(DocumentQuery)}
         * @throws NullPointerException when there are null parameters
         */
        <T> Optional<T> getSingleResult(DocumentTemplate template, Pagination pagination);

        /**
         * Creates a {@link Page} from pagination
         *
         * @param pagination the pagination
         * @param template   the template
         * @param <T>        the type
         * @return a {@link Page} from instance
         * @throws NullPointerException when there are null parameters
         */
        <T> Page<T> page(DocumentTemplate template, Pagination pagination);

    }

    /**
     * The Document Order whose define the position of the first result to retrieve.
     */
    interface DocumentMapperSkip extends DocumentMapperQueryBuild {

        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentMapperLimit limit(long limit);
    }

    /**
     * The Document Where whose define the condition in the query.
     */
    interface DocumentMapperWhere extends DocumentMapperQueryBuild {


        /**
         * Starts a new condition in the select using {@link jakarta.nosql.document.DocumentCondition#and(jakarta.nosql.document.DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentMapperNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentMapperNameCondition and(String name);

        /**
         * Appends a new condition in the select using {@link jakarta.nosql.document.DocumentCondition#or(jakarta.nosql.document.DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentMapperNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentMapperNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        DocumentMapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        DocumentMapperLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentMapperOrder orderBy(String name);
    }
}
