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

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * To either insert or overrides values from a key-value database use the <b>PUT</b> statement.
 */
public interface PutQuery extends Query {

    /**
     * The key
     * @return the key
     */
    QueryValue<?> getKey();

    /**
     * The value
     * @return the value
     */
    QueryValue<?> getValue();

    /**
     * This duration set a time for data in an entity to expire. It defines the time to live of an object in a database.
     * @return the TTL otherwise {@link Optional#empty()}
     */
    Optional<Duration> getTtl();


    /**
     * Obtains an instance of {@link GetQuery} from a text string.
     *
     * @param query the query
     * @return {@link GetQuery} instance
     * @throws NullPointerException                    when the query is null
     * @throws QuerySyntaxException                    if the text cannot be parsed
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     */
    static PutQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return ServiceLoaderProvider.get(PutQueryProvider.class,
                ()-> ServiceLoader.load(PutQueryProvider.class)).apply(query);
    }


    /**
     * A provider to {@link PutQuery}
     */
    interface PutQueryProvider extends Function<String, PutQuery> {


    }
}
