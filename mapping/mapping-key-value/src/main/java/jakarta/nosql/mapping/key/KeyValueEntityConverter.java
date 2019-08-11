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
import jakarta.nosql.mapping.Id;
import jakarta.nosql.mapping.IdNotFoundException;

/**
 * This interface represents the converter between an entity and the {@link KeyValueEntity}
 */
public interface KeyValueEntityConverter {

    /**
     * Converts the instance entity to {@link KeyValueEntity}
     *
     * @param entityInstance the instnace
     * @return a {@link KeyValueEntity} instance
     * @throws IdNotFoundException when the entityInstance hasn't a field with {@link Id}
     * @throws NullPointerException when the entityInstance is null
     */
    KeyValueEntity toKeyValue(Object entityInstance);

    /**
     * Converts a {@link KeyValueEntity} to entity
     *
     * @param entityClass the entity class
     * @param entity      the {@link KeyValueEntity} to be converted
     * @param <T>         the entity type
     * @return the instance from {@link KeyValueEntity}
     * @throws IdNotFoundException when the entityInstance hasn't a field with {@link Id}
     * @throws NullPointerException when the entityInstance is null
     */
    <T> T toEntity(Class<T> entityClass, KeyValueEntity entity);

}
