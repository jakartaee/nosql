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
package jakarta.nosql.document;


import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;

/**
 * This interface represents the manager of events. When an entity be either saved or updated an event will be fired. This order gonna be:
 * 1) firePreEntity
 * 2) firePreDocumentEntity
 * 3) firePreDocument
 * 4) firePostDocument
 * 5) firePostEntity
 * 6) firePostDocumentEntity
 *
 * @see DocumentWorkflow
 */
public interface DocumentEventPersistManager {

    /**
     * Fire an event after the conversion of the entity to communication API model.
     *
     * @param entity the entity
     */
    void firePreDocument(DocumentEntity entity);

    /**
     * Fire an event after the response from communication layer
     *
     * @param entity the entity
     */
    void firePostDocument(DocumentEntity entity);

    /**
     * Fire an event once the method is called
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreEntity(T entity);

    /**
     * Fire an event after convert the {@link DocumentEntity},
     * from database response, to Entity.
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostEntity(T entity);

    /**
     * Fire an event after firePreEntity
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreDocumentEntity(T entity);

    /**
     * Fire an event after firePostEntity
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostDocumentEntity(T entity);


    /**
     * Fire an event before the query is executed
     *
     * @param query the query
     */
    void firePreQuery(DocumentQuery query);

    /**
     * Fire an event before the delete query is executed
     *
     * @param query the query
     */
    void firePreDeleteQuery(DocumentDeleteQuery query);

}
