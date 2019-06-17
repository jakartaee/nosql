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
package jakarta.nosql.mapping.column;


import jakarta.nosql.column.ColumnEntity;

import java.util.function.UnaryOperator;

/**
 * This implementation defines the workflow to insert an Entity on {@link ColumnTemplate}.
 * The default implementation follows:
 *  <p>{@link ColumnEventPersistManager#firePreEntity(Object)}</p>
 *  <p>{@link ColumnEventPersistManager#firePreColumnEntity(Object)}</p>
 *  <p>{@link ColumnEntityConverter#toColumn(Object)}</p>
 *  <p>{@link ColumnEventPersistManager#firePreColumn(ColumnEntity)}</p>
 *  <p>Database alteration</p>
 *  <p>{@link ColumnEventPersistManager#firePostColumn(ColumnEntity)}</p>
 *  <p>{@link ColumnEventPersistManager#firePostEntity(Object)}</p>
 *  <p>{@link ColumnEventPersistManager#firePostColumnEntity(Object)}</p>
 */
public interface ColumnWorkflow {

    /**
     * Executes the workflow to do an interaction on a database column family.
     *
     * @param entity the entity to be saved
     * @param action the alteration to be executed on database
     * @param <T>    the entity type
     * @return after the workflow the the entity response
     * @see ColumnTemplate#insert(Object, java.time.Duration) ColumnTemplate#insert(Object)
     * ColumnTemplate#update(Object)
     */
    <T> T flow(T entity, UnaryOperator<ColumnEntity> action);

}
