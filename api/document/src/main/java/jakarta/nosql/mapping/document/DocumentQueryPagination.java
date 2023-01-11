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

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Page;

import java.util.ServiceLoader;
import java.util.function.BiFunction;

/**
 * A {@link DocumentQuery} that allows select based on pagination.
 */
public interface DocumentQueryPagination extends DocumentQuery {

    /**
     * Returns the {@link DocumentQueryPagination} requesting the next {@link DocumentQueryPagination}.
     *
     * @return the next {@link DocumentQueryPagination}
     */
    DocumentQueryPagination next();

    /**
     * Returns the {@link Pagination} of the current {@link Page}
     *
     * @return a current {@link Pagination}
     */
    Pagination getPagination();


    /**
     * Creates a new instance of {@link DocumentQueryPagination}
     *
     * @param query      the query
     * @param pagination the pagination
     * @return a {@link DocumentQueryPagination} instance
     * @throws NullPointerException when there is null parameter
     */
    static DocumentQueryPagination of(DocumentQuery query, Pagination pagination) {
        return ServiceLoaderProvider.get(DocumentQueryPaginationProvider.class,
                ()-> ServiceLoader.load(DocumentQueryPaginationProvider.class))
                .apply(query, pagination);
    }


    /**
     * A provider {@link DocumentQueryPagination} of where given two parameters:
     * The first is {@link DocumentQuery}
     * The second is {@link Pagination}
     * it returns an instance {@link DocumentQueryPagination}
     */
    interface DocumentQueryPaginationProvider extends BiFunction<DocumentQuery, Pagination, DocumentQueryPagination> {

    }
}
