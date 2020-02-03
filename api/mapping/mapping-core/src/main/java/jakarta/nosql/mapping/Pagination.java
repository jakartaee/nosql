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
package jakarta.nosql.mapping;

import jakarta.nosql.ServiceLoaderProvider;

import java.util.function.Function;

/**
 * Pagination is the process of separating print or digital content into discrete pages.
 * This instance represents this pagination process.
 */
public interface Pagination {

    /**
     * Returns the page to be returned.
     *
     * @return the page to be returned.
     */
    long getPageNumber();

    /**
     * Returns the number of items to be returned.
     *
     * @return the number of items of that page
     */
    long getPageSize();

    /**
     * @return The maximum number of results the select object was set to retrieve.
     * According to the underlying page and page size.
     */
    long getLimit();

    /**
     * @return The position of the first result the select object was set to retrieve.
     * According to the underlying page and page size.
     */
    long getSkip();


    /**
     * Returns the {@link Pagination} requesting the next {@link Pagination}.
     *
     * @return the next pagination
     */
    Pagination next();

    /**
     * Returns a pagination instance that is read-only, in other words, that is not allowed to use the {@link Pagination#next()}.
     *
     * @return a read-onlye {@link Pagination} instance
     */
    Pagination unmodifiable();

    /**
     * A builder to {@link Pagination}, as the first step it defines the page number or the page index that starts from page one.
     *
     * @param page the page index
     * @return a new {@link PaginationBuilder} instance
     * @throws IllegalArgumentException when page is lesser equals than zero
     */
    static PaginationBuilder page(long page) {
        return ServiceLoaderProvider.get(PaginationBuilderProvider.class).apply(page);
    }

    /**
     * The builder of {@link Pagination}
     */
    interface PaginationBuilder {

        /**
         * Defines the size of a pagination
         *
         * @param size the size of pagination
         * @return a {@link Pagination} instance
         * @throws IllegalArgumentException when size is either zero or negative
         */
        Pagination size(long size);
    }

    /**
     * A provider of {@link PaginationBuilder} where it will create from the {@link Long} value
     */
    interface PaginationBuilderProvider extends Function<Long, PaginationBuilder> {

    }
}
