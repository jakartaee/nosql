/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package jakarta.nosql.communication.spi;


import java.util.stream.Stream;

/**
 * Represents a fluent, provider-defined select operation.
 *
 * <p>This API defines the structural flow of a select operation without
 * standardizing query semantics, operators, ordering rules, pagination
 * behavior, or evaluation guarantees. All semantics are provider-defined.</p>
 *
 * <p>Condition and ordering tokens are opaque to this specification and must
 * be created by the underlying provider.</p>
 *
 * <p>A selection target must be specified via {@link #from(String)} before
 * applying conditions or executing the operation.</p>
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * Stream<ProviderStructure> result =
 *         manager.select()
 *                .from("orders")
 *                .where(provider.condition("status", "ACTIVE"))
 *                .orderBy(provider.order("createdAt"))
 *                .limit(10)
 *                .fetch();
 * }</pre>
 *
 * @param <T> the provider-specific structure returned by the operation
 */
public interface SelectExecutor<T> {

    /**
     * Defines the logical selection target for the operation.
     *
     * <p>The interpretation of {@code name} is provider-defined. It may
     * represent a collection, container, table, bucket, graph, or any other
     * logical structure supported by the underlying database.</p>
     *
     * <pre>{@code
     * manager.select()
     *        .from("orders")
     *        .fetch();
     * }</pre>
     *
     * @param name provider-defined logical identifier
     * @return the next step in the select operation
     * @throws NullPointerException if {@code name} is {@code null}
     * @throws UnsupportedOperationException if the provider
     * does not support scoped selection
     */
    From<T> from(String name);

    /**
     * Represents a select operation after a target has been defined.
     */
    interface From<T> extends FinalStep<T> {

        /**
         * Applies an initial provider-defined condition token.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .where(provider.condition("status", "ACTIVE"))
         *        .fetch();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the next step in the select operation
         * @throws NullPointerException if {@code condition} is {@code null}
         * @throws UnsupportedOperationException if the provider
         * does not support conditional selection
         */
        Junction<T> where(Condition condition);
    }

    /**
     * Represents a logical junction after at least one condition
     * has been applied.
     */
    interface Junction<T> extends FinalStep<T> {

        /**
         * Applies a logical AND with another provider-defined condition.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .where(active)
         *        .and(highPriority)
         *        .fetch();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the next junction
         * @throws NullPointerException if {@code condition} is {@code null}
         * @throws UnsupportedOperationException if the provider
         * does not support logical composition
         */
        Junction<T> and(Condition condition);

        /**
         * Applies a logical OR with another provider-defined condition.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .where(active)
         *        .or(pending)
         *        .fetch();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the next junction
         * @throws NullPointerException if {@code condition} is {@code null}
         * @throws UnsupportedOperationException if the provider
         * does not support logical composition
         */
        Junction<T> or(Condition condition);
    }

    /**
     * Final selectable steps available after a target has been defined.
     */
    interface FinalStep<T> {

        /**
         * Applies provider-defined ordering to the select operation.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .orderBy(provider.order("createdAt"))
         *        .fetch();
         * }</pre>
         *
         * @param order provider-defined order token
         * @return the ordering step
         * @throws NullPointerException if {@code order} is {@code null}
         * @throws UnsupportedOperationException if ordering
         * is not supported by the provider
         */
        Ordering<T> orderBy(Order order);

        /**
         * Limits the maximum number of results returned.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .limit(10)
         *        .fetch();
         * }</pre>
         *
         * @param limit maximum number of results
         * @return pagination step
         * @throws IllegalArgumentException if {@code limit} is negative
         * @throws UnsupportedOperationException if pagination
         * is not supported by the provider
         */
        Pagination<T> limit(long limit);

        /**
         * Executes the select operation.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .fetch();
         * }</pre>
         *
         * @return a stream of provider-specific structures
         * @throws UnsupportedOperationException if the provider
         * does not support select execution
         */
        Stream<T> fetch();
    }

    /**
     * Ordering step allowing multiple provider-defined order tokens.
     */
    interface Ordering<T> extends FinalStep<T> {

        /**
         * Applies an additional provider-defined ordering.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .orderBy(created)
         *        .then(priority)
         *        .fetch();
         * }</pre>
         *
         * @param order provider-defined order token
         * @return ordering step
         * @throws NullPointerException if {@code order} is {@code null}
         * @throws UnsupportedOperationException if the provider
         * does not support multiple ordering
         */
        Ordering<T> then(Order order);
    }

    /**
     * Pagination step where limit and skip are non-repeatable.
     */
    interface Pagination<T> {

        /**
         * Skips a number of results.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .limit(10)
         *        .skip(5)
         *        .fetch();
         * }</pre>
         *
         * @param skip number of results to skip
         * @return final step
         * @throws IllegalArgumentException if {@code skip} is negative
         * @throws UnsupportedOperationException if skipping
         * is not supported by the provider
         */
        FinalStep<T> skip(long skip);

        /**
         * Executes the select operation.
         *
         * <pre>{@code
         * manager.select()
         *        .from("orders")
         *        .limit(10)
         *        .fetch();
         * }</pre>
         *
         * @return a stream of provider-specific structures
         * @throws UnsupportedOperationException if the provider
         * does not support select execution
         */
        Stream<T> fetch();
    }
}