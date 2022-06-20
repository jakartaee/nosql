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
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * Updating an entity is done using an <b>UPDATE</b> statement.
 */
public interface UpdateQuery extends Query {

    /**
     * The entity name
     * @return the entity name
     */
    String getEntity();

    /**
     * The list of changes as conditions. Each condition will use the equals operator, {@link Operator#EQUALS},
     *  e.g., name = "any name"
     * @return the conditions
     */
    List<Condition> getConditions();

    /**
     * Returns the value to update when the query uses JSON value instead of Conditions.
     * In an insert, an operation is not able to use both: {@link UpdateQuery#getConditions()} and
     * {@link UpdateQuery#getValue()}.
     * Therefore, execution will use just one operation type.
     * @return a {@link JSONQueryValue} or {@link Optional#empty()} when it uses {@link UpdateQuery#getConditions()}
     */
    Optional<JSONQueryValue> getValue();

    /**
     * Obtains an instance of {@link UpdateQuery} from a text string.
     *
     * @param query the query
     * @return {@link UpdateQuery} instance
     * @throws NullPointerException                    when the query is null
     * @throws QuerySyntaxException                    if the text cannot be parsed
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     */
    static UpdateQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return ServiceLoaderProvider.get(UpdateQueryProvider.class,
                ()-> ServiceLoader.load(UpdateQueryProvider.class)).apply(query);
    }


    /**
     * A provider to {@link UpdateQuery}
     */
    interface UpdateQueryProvider extends Function<String, UpdateQuery> {

    }
}
