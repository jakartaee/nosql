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
 * standardizing query semantics, operators, or evaluation behavior.
 * All condition and ordering semantics are provider-defined.</p>
 *
 * <p>Condition and ordering tokens are opaque to this specification and
 * must be created by the underlying provider.</p>
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
     * <pre>{@code
     * Condition active =
     *         provider.condition("status", "ACTIVE");
     *
     * Stream<ProviderStructure> result =
     *         manager.select()
     *                .where(active)
     *                .fetch();
     * }</pre>
     *
     * @param condition provider-defined condition token
     * @return the next step in the select operation
     * @throws NullPointerException if {@code condition} is {@code null}
     */
    Junction<T> where(Condition condition);

    /**
     * Executes the select operation without applying any conditions.
     *
     * <pre>{@code
     * Stream<ProviderStructure> result =
     *         manager.select().fetch();
     * }</pre>
     *
     * @return a stream of provider-specific structures
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
         * <pre>{@code
         * Condition active =
         *         provider.condition("status", "ACTIVE");
         *
         * Condition highValue =
         *         provider.condition("total", 100);
         *
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(active)
         *                .and(highValue)
         *                .fetch();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the next junction
         * @throws NullPointerException if {@code condition} is {@code null}
         */
        Junction<T> and(Condition condition);

        /**
         * Applies a logical OR with another provider-defined condition.
         *
         * <pre>{@code
         * Condition pending =
         *         provider.condition("status", "PENDING");
         *
         * Condition failed =
         *         provider.condition("status", "FAILED");
         *
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(pending)
         *                .or(failed)
         *                .fetch();
         * }</pre>
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
         * <pre>{@code
         * Order byCreated =
         *         provider.order("createdAt");
         *
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(provider.condition("status", "ACTIVE"))
         *                .orderBy(byCreated)
         *                .fetch();
         * }</pre>
         *
         * @param order provider-defined order token
         * @return the ordering step
         * @throws NullPointerException if {@code order} is {@code null}
         */
        Ordering<T> orderBy(Order order);

        /**
         * Limits the maximum number of results returned.
         *
         * <pre>{@code
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(provider.condition("status", "ACTIVE"))
         *                .limit(10)
         *                .fetch();
         * }</pre>
         *
         * @param limit maximum number of results
         * @return pagination step
         * @throws IllegalArgumentException if {@code limit} is negative
         */
        Pagination<T> limit(long limit);

        /**
         * Executes the select operation.
         *
         * <pre>{@code
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(provider.condition("status", "ACTIVE"))
         *                .fetch();
         * }</pre>
         *
         * @return a stream of provider-specific structures
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
         * Order byCreated =
         *         provider.order("createdAt");
         *
         * Order byPriority =
         *         provider.order("priority");
         *
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(provider.condition("status", "ACTIVE"))
         *                .orderBy(byCreated)
         *                .then(byPriority)
         *                .fetch();
         * }</pre>
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
         * <pre>{@code
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(provider.condition("status", "ACTIVE"))
         *                .limit(20)
         *                .skip(5)
         *                .fetch();
         * }</pre>
         *
         * @param skip number of results to skip
         * @return final step
         * @throws IllegalArgumentException if {@code skip} is negative
         */
        FinalStep<T> skip(long skip);

        /**
         * Executes the select operation.
         *
         * <pre>{@code
         * Stream<ProviderStructure> result =
         *         manager.select()
         *                .where(provider.condition("status", "ACTIVE"))
         *                .limit(10)
         *                .fetch();
         * }</pre>
         *
         * @return a stream of provider-specific structures
         */
        Stream<T> fetch();
    }
}
