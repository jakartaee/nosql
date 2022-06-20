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
import jakarta.nosql.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * The select statement reads one or more fields for one or more entities.
 * It returns a result-set of the entities matching the request, where each entity contains the fields
 * for corresponding to the query.
 */
public interface SelectQuery extends Query {

    /**
     * The fields that will retrieve in this query, if this fields is empty, this query will retrieve the whole entity.
     *
     * @return the fields list
     */
    List<String> getFields();

    /**
     * The entity name
     *
     * @return the entity name
     */
    String getEntity();

    /**
     * The condition at this {@link SelectQuery}, if the Where is empty that means may retrieve the whole entities.
     *
     * @return the {@link Where} entity otherwise {@link Optional#empty()}
     */
    Optional<Where> getWhere();

    /**
     * Statement defines where the query should start
     *
     * @return the number to skip, otherwise either negative value or zero
     */
    long getSkip();

    /**
     * Statement limits the number of rows returned by a query,
     *
     * @return the maximum of result, otherwise either negative value or zero
     */
    long getLimit();

    /**
     * The list of orders, it is used to sort the result-set in ascending or descending order.
     *
     * @return the order list
     */
    List<Sort> getOrderBy();

    /**
     * Obtains an instance of {@link SelectQuery} from a text string.
     *
     * @param query the query
     * @return {@link SelectQuery} instance
     * @throws NullPointerException                    when the query is null
     * @throws QuerySyntaxException                    if the text cannot be parsed
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     */
    static SelectQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return ServiceLoaderProvider.get(SelectQueryProvider.class,
                ()-> ServiceLoader.load(SelectQueryProvider.class)).apply(query);
    }


    /**
     * A provider to {@link SelectQuery}
     */
    interface SelectQueryProvider extends Function<String, SelectQuery> {

    }
}
