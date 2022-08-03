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
package jakarta.nosql.mapping.keyvalue;


import jakarta.nosql.keyvalue.KeyValueEntity;

import java.util.function.UnaryOperator;

/**
 * This implementation defines the workflow to insert an Entity on {@link KeyValueTemplate}.
 * The default implementation follows:
 *  <p>{@link KeyValueEventPersistManager#firePreEntity(Object)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePreKeyValueEntity(Object)}</p>
 *  <p>{@link KeyValueEntityConverter#toKeyValue(Object)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePreKeyValue(KeyValueEntity)}</p>
 *  <p>Database alteration</p>
 *  <p>{@link KeyValueEventPersistManager#firePostKeyValue(KeyValueEntity)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePostEntity(Object)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePostKeyValueEntity(Object)}</p>
 */
public interface KeyValueWorkflow {

    /**
     * Executes the workflow to do an interaction on a database key-value.
     *
     * @param entity the entity to be saved
     * @param action the alteration to be executed on database
     * @param <T>    the entity type
     * @return after the workflow the entity response
     * @see KeyValueTemplate#put(Object, java.time.Duration)  {@link KeyValueTemplate#put(Object)}
     * DocumentTemplate#update(Object)
     */
    <T> T flow(T entity, UnaryOperator<KeyValueEntity> action);
}
