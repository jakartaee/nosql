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
 * All condition semantics are provider-defined.</p>
 *
 * <p>Condition tokens are opaque to this specification and must be
 * created by the provider.</p>
 *
 * @param <T> the provider-specific structure returned by the operation
 */
public interface SelectExecutor<T> {


    /**
     * Applies a provider-defined condition token.
     *
     * <pre>{@code
     * Condition condition = provider.eq("status", "ACTIVE");
     *
     * manager.select()
     *        .where(condition)
     *        .fetch();
     * }</pre>
     *
     * @param condition provider-defined condition token
     * @return the next step in the select operation
     * @throws NullPointerException if {@code condition} is null
     */
    Junction<T> where(Condition condition);

    /**
     * Executes the select operation.
     *
     * @return a stream of provider-specific structures
     */
    Stream<T> fetch();

    /**
     * Represents a logical junction after at least one condition.
     */
    interface Junction<T> extends FinalStep<T> {

        /**
         * Applies a logical AND with another provider-defined condition.
         *
         * @param condition provider-defined condition token
         * @return the next junction
         */
        Junction<T> and(Condition condition);

        /**
         * Applies a logical OR with another provider-defined condition.
         *
         * @param condition provider-defined condition token
         * @return the next junction
         */
        Junction<T> or(Condition condition);
    }

    /**
     * Final selectable steps.
     */
    interface FinalStep<T> {

        /**
         * Applies provider-defined ordering.
         *
         * @param order provider-defined order token
         * @return the ordering step
         */
        Ordering<T> orderBy(Order order);

        /**
         * Limits the number of results.
         *
         * @param limit maximum number of results
         * @return pagination step
         */
        Pagination<T> limit(long limit);

        /**
         * Executes the select operation.
         *
         * @return a stream of provider-specific structures
         */
        Stream<T> fetch();
    }

    /**
     * Ordering step.
     */
    interface Ordering<T> extends FinalStep<T> {

        /**
         * Applies additional ordering.
         *
         * @param order provider-defined order token
         * @return ordering step
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
         * @param skip number of results to skip
         * @return final step
         */
        FinalStep<T> skip(long skip);

        /**
         * Executes the select operation.
         *
         * @return a stream of provider-specific structures
         */
        Stream<T> fetch();
    }
}
