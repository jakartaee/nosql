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
 * Represents a fluent, provider-defined select operation based on
 * opaque condition tokens.
 *
 * <p>This API defines the structural flow of a select operation without
 * standardizing query semantics, operators, ordering rules, or evaluation
 * behavior. All semantics are provider-defined.</p>
 *
 * <p>Condition and ordering tokens are opaque to this specification and must
 * be created by the underlying provider.</p>
 *
 * <p>Providers are not required to support conditional selection, ordering,
 * pagination, or streaming. If a requested capability is not supported,
 * implementations may throw {@link UnsupportedOperationException} during
 * execution.</p>
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * Condition active =
 *         provider.condition("status", "ACTIVE");
 *
 * Condition highValue =
 *         provider.condition("total", 100);
 *
 * Order byCreated =
 *         provider.order("createdAt");
 *
 * Stream<ProviderStructure> result =
 *         manager.select()
 *                .where(active)
 *                .and(highValue)
 *                .orderBy(byCreated)
 *                .limit(10)
 *                .skip(5)
 *                .fetch();
 * }</pre>
 *
 * @param <T> the provider-specific structure returned by the operation
 */
public interface SelectExecutor<T> {

    /**
     * Applies an initial provider-defined condition token.
     *
     * <p>Support for conditional select operations is provider-defined.
     * If conditions are not supported by the underlying database,
     * execution of the select operation may result in
     * {@link UnsupportedOperationException}.</p>
     *
     * @param condition provider-defined condition token
     * @return the next step in the select operation
     * @throws NullPointerException if {@code condition} is {@code null}
     */
    Junction<T> where(Condition condition);

    /**
     * Executes the select operation without applying any conditions.
     *
     * <p>Unconditional selection may not be supported by all providers
     * and may result in {@link UnsupportedOperationException}.</p>
     *
     * @return a stream of provider-specific structures
     * @throws UnsupportedOperationException if selection is not supported
     */
    Stream<T> fetch();

    /**
     * Represents a logical junction after at least one condition
     * has been applied.
     */
    interface Junction<T> extends FinalStep<T> {

        /**
         * Applies a logical AND with another provider-defined condition.
         *
         * <p>Logical composition of conditions is provider-defined and
         * may not be supported by all implementations.</p>
         *
         * @param condition provider-defined condition token
         * @return the next junction
         * @throws NullPointerException if {@code condition} is {@code null}
         */
        Junction<T> and(Condition condition);

        /**
         * Applies a logical OR with another provider-defined condition.
         *
         * <p>Logical composition of conditions is provider-defined and
         * may not be supported by all implementations.</p>
         *
         * @param condition provider-defined condition token
         * @return the next junction
         * @throws NullPointerException if {@code condition} is {@code null}
         */
        Junction<T> or(Condition condition);
    }

    /**
     * Final selectable steps available after at least one condition
     * has been applied.
     */
    interface FinalStep<T> {

        /**
         * Applies provider-defined ordering to the select operation.
         *
         * <p>Support for ordering is provider-defined and may not be
         * available in all databases.</p>
         *
         * @param order provider-defined order token
         * @return the ordering step
         * @throws NullPointerException if {@code order} is {@code null}
         */
        Ordering<T> orderBy(Order order);

        /**
         * Limits the maximum number of results returned.
         *
         * <p>Support for limiting select operations is provider-defined
         * and may not be available in all databases.</p>
         *
         * @param limit maximum number of results
         * @return pagination step
         * @throws IllegalArgumentException if {@code limit} is negative
         */
        Pagination<T> limit(long limit);

        /**
         * Executes the select operation.
         *
         * <p>If the underlying database does not support conditional selection,
         * logical condition composition, ordering, pagination, or streaming,
         * this method may throw {@link UnsupportedOperationException}.</p>
         *
         * @return a stream of provider-specific structures
         * @throws UnsupportedOperationException if the provider
         * does not support the select operation
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
         * <p>All providers may not support multiple ordering tokens.</p>
         *
         * @param order provider-defined order token
         * @return ordering step
         * @throws NullPointerException if {@code order} is {@code null}
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
         * <p>Support for skipping results is provider-defined and may not
         * be available in all databases.</p>
         *
         * @param skip number of results to skip
         * @return final step
         * @throws IllegalArgumentException if {@code skip} is negative
         */
        FinalStep<T> skip(long skip);

        /**
         * Executes the select operation.
         *
         * <p>If the underlying database does not support pagination or
         * streaming, this method may throw
         * {@link UnsupportedOperationException}.</p>
         *
         * @return a stream of provider-specific structures
         * @throws UnsupportedOperationException if the provider
         * does not support the select operation
         */
        Stream<T> fetch();
    }
}