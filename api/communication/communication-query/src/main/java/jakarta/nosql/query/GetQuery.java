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

package jakarta.nosql.query;

import jakarta.nosql.ServiceLoaderProvider;

import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * To retrieve one or more entities use the <b>GET</b> statement.
 * This query is particular to a key-value database.
 */
public interface GetQuery extends Query {

    /**
     * The keys to being retrieved from the query
     *
     * @return the keys
     */
    List<QueryValue<?>> getKeys();

    /**
     * Obtains an instance of {@link GetQuery} from a text string.
     *
     * @param query the query
     * @return {@link GetQuery} instance
     * @throws NullPointerException                    when the query is null
     * @throws QuerySyntaxException                    if the text cannot be parsed
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     */
    static GetQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return ServiceLoaderProvider.get(GetQueryProvider.class,
                () -> ServiceLoader.load(GetQueryProvider.class)).apply(query);
    }

    /**
     * Returns the {@link GetQueryProvider} instance
     * @return the GetQueryProvider instance
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     */
    static GetQueryProvider getProvider() {
        return ServiceLoaderProvider.get(GetQueryProvider.class,
                () -> ServiceLoader.load(GetQueryProvider.class));
    }

    /**
     * A provider to {@link GetQuery}, this provider converts text into {@link GetQuery}
     */
    interface GetQueryProvider extends Function<String, GetQuery> {

    }
}
