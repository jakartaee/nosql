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
 * Represents a fluent, provider-defined delete operation.
 *
 * <p>This API defines the structural flow of a delete operation without
 * standardizing delete semantics, matching rules, or execution guarantees.
 * All semantics are provider-defined.</p>
 *
 * <p>Condition tokens are opaque to this specification and must be created
 * by the underlying provider.</p>
 *
 * <p>A delete target must be specified via {@link #from(String)} before
 * applying conditions or executing the operation.</p>
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * manager.delete()
 *        .from("orders")
 *        .where(provider.condition("status", "EXPIRED"))
 *        .and(provider.condition("priority", "LOW"))
 *        .execute();
 * }</pre>
 */
public interface DeleteExecutor {

    /**
     * Defines the logical delete target for the operation.
     *
     * <p>The interpretation of {@code name} is provider-defined. It may
     * represent a collection, container, table, bucket, or any other
     * logical structure supported by the underlying database.</p>
     *
     * <pre>{@code
     * manager.delete()
     *        .from("orders")
     *        .execute();
     * }</pre>
     *
     * @param name provider-defined logical identifier
     * @return the next step in the delete operation
     * @throws NullPointerException if {@code name} is {@code null}
     */
    From from(String name);

    /**
     * Represents a delete operation after a target has been defined.
     */
    interface From {

        /**
         * Applies an initial provider-defined condition token.
         *
         * <pre>{@code
         * manager.delete()
         *        .from("orders")
         *        .where(provider.condition("status", "EXPIRED"))
         *        .execute();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the next step in the delete operation
         * @throws NullPointerException if {@code condition} is {@code null}
         */
        Junction where(Condition condition);

        /**
         * Executes the delete operation without applying any conditions.
         *
         * <pre>{@code
         * manager.delete()
         *        .from("orders")
         *        .execute();
         * }</pre>
         *
         * @throws UnsupportedOperationException if the provider does not support the deleter operation
         * or it contains any condition that is not provided.
         */
        void execute();
    }

    /**
     * Represents a logical junction after at least one condition
     * has been applied.
     */
    interface Junction {

        /**
         * Applies a logical AND with another provider-defined condition.
         *
         * <pre>{@code
         * manager.delete()
         *        .from("orders")
         *        .where(expired)
         *        .and(lowPriority)
         *        .execute();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the current delete operation
         * @throws NullPointerException if {@code condition} is {@code null}
         * @throws UnsupportedOperationException if the provider
         * does not support logical composition
         */
        Junction and(Condition condition);

        /**
         * Applies a logical OR with another provider-defined condition.
         *
         * <pre>{@code
         * manager.delete()
         *        .from("orders")
         *        .where(expired)
         *        .or(cancelled)
         *        .execute();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the current delete operation
         * @throws NullPointerException if {@code condition} is {@code null}
         * @throws UnsupportedOperationException if the provider
         * does not support logical composition
         */
        Junction or(Condition condition);

        /**
         * Executes the delete operation.
         *
         * <pre>{@code
         * manager.delete()
         *        .from("orders")
         *        .where(expired)
         *        .execute();
         * }</pre>
         *
         * @throws UnsupportedOperationException if the provider does not support the deleter operation
         * or it contains any condition that is not provided.
         */
        void execute();
    }
}
