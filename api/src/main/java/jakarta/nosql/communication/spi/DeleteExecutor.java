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

/**
 * Represents a fluent, provider-defined delete operation based on
 * opaque condition tokens.
 *
 * <p>This API defines the structural flow of a delete operation without
 * standardizing delete semantics, operators, or evaluation behavior.
 * All condition semantics are provider-defined.</p>
 *
 * <p>Condition tokens are opaque to this specification and must be
 * created by the underlying provider.</p>
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * Condition expired =
 *         provider.condition("status", "EXPIRED");
 *
 * Condition lowPriority =
 *         provider.condition("priority", "LOW");
 *
 * manager.delete()
 *        .where(expired)
 *        .and(lowPriority)
 *        .limit(100)
 *        .execute();
 * }</pre>
 */
public interface DeleteExecutor {

    /**
     * Applies an initial provider-defined condition token.
     *
     * @param condition provider-defined condition token
     * @return the next step in the delete operation
     * @throws NullPointerException if {@code condition} is {@code null}
     */
    Junction where(Condition condition);

    /**
     * Executes the delete operation without applying any conditions.
     *
     * <p>The behavior of unconditional delete operations is provider-defined
     * and may be restricted or unsupported.</p>
     */
    void execute();

    /**
     * Represents a logical junction after at least one condition
     * has been applied.
     */
    interface Junction extends FinalStep {

        /**
         * Applies a logical AND with another provider-defined condition.
         *
         * @param condition provider-defined condition token
         * @return the next junction
         * @throws NullPointerException if {@code condition} is {@code null}
         */
        Junction and(Condition condition);

        /**
         * Applies a logical OR with another provider-defined condition.
         *
         * @param condition provider-defined condition token
         * @return the next junction
         * @throws NullPointerException if {@code condition} is {@code null}
         */
        Junction or(Condition condition);
    }

    /**
     * Final selectable steps available after at least one condition
     * has been applied.
     */
    interface FinalStep {

        /**
         * Limits the maximum number of structures affected by the delete
         * operation.
         *
         * <p>Support for limiting delete operations is provider-defined.</p>
         *
         * @param limit maximum number of structures to delete
         * @return pagination step
         * @throws IllegalArgumentException if {@code limit} is negative
         */
        Pagination limit(long limit);

        /**
         * Executes the delete operation.
         */
        void execute();
    }

    /**
     * Pagination step where limit is non-repeatable.
     */
    interface Pagination {

        /**
         * Executes the delete operation.
         */
        void execute();
    }
}