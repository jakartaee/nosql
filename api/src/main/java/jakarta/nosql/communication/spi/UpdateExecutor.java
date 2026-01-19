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
 * Represents a fluent, provider-defined update operation.
 *
 * <p>This API defines the structural flow of an update operation without
 * standardizing update semantics, mutation models, matching rules, or
 * execution guarantees. All semantics are provider-defined.</p>
 *
 * <p>Update and condition tokens are opaque to this specification and must
 * be created by the underlying provider.</p>
 *
 * <p>An update target must be specified via {@link #from(String)} before
 * applying update tokens or executing the operation.</p>
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * UpdateExecutor.Update promote =
 *         provider.update("priority", "HIGH");
 *
 * UpdateExecutor.Update markReviewed =
 *         provider.update("reviewed", true);
 *
 * manager.update()
 *        .from("orders")
 *        .set(promote)
 *        .set(markReviewed)
 *        .where(provider.condition("status", "ACTIVE"))
 *        .execute();
 * }</pre>
 */
public interface UpdateExecutor {

    /**
     * Defines the logical update target for the operation.
     *
     * <p>The interpretation of {@code name} is provider-defined. It may
     * represent a collection, container, table, bucket, or any other
     * logical structure supported by the underlying database.</p>
     *
     * <pre>{@code
     * manager.update()
     *        .from("orders")
     *        .execute();
     * }</pre>
     *
     * @param name provider-defined logical identifier
     * @return the next step in the update operation
     * @throws NullPointerException if {@code name} is {@code null}
     */
    From from(String name);

    /**
     * Represents an update operation after a target has been defined.
     */
    interface From {

        /**
         * Applies a provider-defined update token.
         *
         * <p>This method may be called multiple times to apply more than one
         * update token, depending on provider capabilities.</p>
         *
         * <pre>{@code
         * manager.update()
         *        .from("orders")
         *        .set(provider.update("priority", "HIGH"))
         *        .execute();
         * }</pre>
         *
         * @param update provider-defined update token
         * @return the current update operation
         * @throws NullPointerException if {@code update} is {@code null}
         */
        From set(Update update);

        /**
         * Applies a provider-defined condition token used to restrict
         * which structures are updated.
         *
         * <pre>{@code
         * manager.update()
         *        .from("orders")
         *        .set(provider.update("priority", "HIGH"))
         *        .where(provider.condition("status", "ACTIVE"))
         *        .execute();
         * }</pre>
         *
         * @param condition provider-defined condition token
         * @return the current update operation
         * @throws NullPointerException if {@code condition} is {@code null}
         */
        From where(Condition condition);

        /**
         * Executes the update operation.
         *
         * <pre>{@code
         * manager.update()
         *        .from("orders")
         *        .set(provider.update("archived", true))
         *        .execute();
         * }</pre>
         *
         * @throws UnsupportedOperationException if the provider
         * does not support the update operation or it contains any condition not supported by the provider
         */
        void execute();
    }

    /**
     * Represents a provider-defined update token.
     *
     * <p>An {@code Update} describes a mutation to be applied during an update
     * operation. The structure, semantics, and execution behavior of this token
     * are entirely provider-defined.</p>
     *
     * <p>This specification does not define how updates are expressed,
     * combined, or applied. Implementations may support partial updates,
     * full replacements, atomic mutations, or other models depending on
     * the underlying database.</p>
     */
    interface Update {
    }
}