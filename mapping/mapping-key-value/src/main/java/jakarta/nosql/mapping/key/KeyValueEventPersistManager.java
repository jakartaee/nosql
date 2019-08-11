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
package jakarta.nosql.mapping.key;


import jakarta.nosql.kv.KeyValueEntity;

/**
 * This interface represent the manager of events. When an entity be either saved or updated an event will be fired. This order gonna be:
 * 1) firePreKeyValue
 * 2) firePreKeyValueEntity
 * 3) firePostKeyValue
 * 4) firePostColumn
 * 5) firePostEntity
 * 6) firePostKeyValueEntity
 *
 * @see KeyValueWorkflow
 */
public interface KeyValueEventPersistManager {

    /**
     * Fire an event after the conversion of the entity to communication API model.
     *
     * @param entity the entity
     */
    void firePreKeyValue(KeyValueEntity entity);

    /**
     * Fire an event after the response from communication layer
     *
     * @param entity the entity
     */
    void firePostKeyValue(KeyValueEntity entity);

    /**
     * Fire an event once the method is called
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreEntity(T entity);

    /**
     * Fire an event after convert the {@link KeyValueEntity},
     * from database response, to Entity.
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostEntity(T entity);


    /**
     * fire an event after the firePostEntity
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreKeyValueEntity(T entity);

    /**
     * Fire the last event
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostKeyValueEntity(T entity);

}

