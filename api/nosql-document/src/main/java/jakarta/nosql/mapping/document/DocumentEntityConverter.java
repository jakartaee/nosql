/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package jakarta.nosql.mapping.document;

import jakarta.nosql.document.DocumentEntity;

/**
 * This interface represents the converter between an entity and the {@link DocumentEntity}
 */
public interface DocumentEntityConverter {

    /**
     * Converts the instance entity to {@link DocumentEntity}
     *
     * @param entity the instance
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when entity is null
     */
    DocumentEntity toDocument(Object entity);

    /**
     * Converts a {@link DocumentEntity} to entity
     *
     * @param type the entity class
     * @param entity      the {@link DocumentEntity} to be converted
     * @param <T>         the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when either type or entity are null
     */
    <T> T toEntity(Class<T> type, DocumentEntity entity);

    /**
     * Converts a {@link DocumentEntity} to entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param type the entity class
     * @param entity         the {@link DocumentEntity} to be converted
     * @param <T>            the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when either type or entity are null
     */
    <T> T toEntity(T type, DocumentEntity entity);

    /**
     * Similar to {@link DocumentEntityConverter#toEntity(Class, DocumentEntity)}, but
     * search the instance type from {@link DocumentEntity#getName()}
     *
     * @param entity the {@link DocumentEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when entity is null
     */
    <T> T toEntity(DocumentEntity entity);
}
