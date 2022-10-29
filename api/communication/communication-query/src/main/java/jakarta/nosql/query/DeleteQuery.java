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

package jakarta.nosql.query;

import jakarta.nosql.ServiceLoaderProvider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * Deleting either an entity or fields uses the <b>DELETE</b> statement
 */
public interface DeleteQuery extends Query {

    /**
     * The fields that will delete in this query, if this fields is empty, this query will remove the whole entity.
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
     * The condition at this {@link DeleteQuery}, if the Where is empty that means will delete the whole entities.
     *
     * @return the {@link Where} entity otherwise {@link Optional#empty()}
     */
    Optional<Where> getWhere();


    /**
     * Obtains an instance of {@link DeleteQuery} from a text string.
     *
     * @param query the query
     * @return {@link DeleteQuery} instance
     * @throws NullPointerException                    when the query is null
     * @throws QuerySyntaxException                    if the text cannot be parsed
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     */
    static DeleteQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return ServiceLoaderProvider.get(DeleteQueryProvider.class,
                ()-> ServiceLoader.load(DeleteQueryProvider.class)).apply(query);
    }

    /**
     * Returns the {@link DeleteQueryProvider} instance
     * @return the DeleteQueryProvider instance
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     */
    static DeleteQueryProvider getProvider() {
        return ServiceLoaderProvider.get(DeleteQueryProvider.class,
                ()-> ServiceLoader.load(DeleteQueryProvider.class));
    }


    /**
     * A provider to {@link DeleteQuery}, this provider converts query text in {@link DeleteQuery}
     */
    interface DeleteQueryProvider extends Function<String, DeleteQuery> {

    }
}
