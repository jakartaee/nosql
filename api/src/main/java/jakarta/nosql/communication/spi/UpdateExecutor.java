package jakarta.nosql.communication.spi;


/**
 * Represents a fluent, provider-defined update operation based on
 * opaque update and condition tokens.
 *
 * <p>This API defines the structural flow of an update operation without
 * standardizing update semantics, operators, matching rules, or evaluation
 * behavior. All update and condition semantics are provider-defined.</p>
 *
 * <p>Update tokens are scoped to this executor and have no meaning
 * outside an update operation.</p>
 *
 * <p>Providers are not required to support conditional updates or bulk
 * updates. If a requested capability is not supported, implementations may
 * throw {@link UnsupportedOperationException} during execution.</p>
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
 * Condition active =
 *         provider.condition("status", "ACTIVE");
 *
 * manager.update()
 *        .set(promote)
 *        .set(markReviewed)
 *        .where(active)
 *        .execute();
 * }</pre>
 */
public interface UpdateExecutor {

    /**
     * Applies a provider-defined update token.
     *
     * <p>This method may be called multiple times to apply more than one
     * update token, depending on provider capabilities.</p>
     *
     * @param update provider-defined update token
     * @return the current update operation
     * @throws NullPointerException if {@code update} is {@code null}
     */
    UpdateExecutor set(Update update);

    /**
     * Applies a provider-defined condition token used to restrict
     * which structures are updated.
     *
     * <p>Support for conditional updates is provider-defined.
     * If conditions are not supported by the underlying database,
     * execution of the update operation may result in
     * {@link UnsupportedOperationException}.</p>
     *
     * @param condition provider-defined condition token
     * @return the current update operation
     * @throws NullPointerException if {@code condition} is {@code null}
     */
    UpdateExecutor where(Condition condition);

    /**
     * Executes the update operation.
     *
     * <p>The behavior of update execution, including atomicity,
     * matching rules, and consistency guarantees, is provider-defined.</p>
     *
     * @throws UnsupportedOperationException if the provider
     * does not support the update operation
     */
    void execute();

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