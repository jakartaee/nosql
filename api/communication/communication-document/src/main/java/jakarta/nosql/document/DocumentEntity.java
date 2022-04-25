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

package jakarta.nosql.document;


import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.TypeSupplier;
import jakarta.nosql.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A document-oriented database, or document store, is a computer program designed for storing, retrieving,
 * and managing document-oriented information, also known as semi-structured data. D
 * ocument-oriented databases are one of the main categories of NoSQL databases, and the
 * popularity of the term "document-oriented database" has grown with the use of the term NoSQL itself.
 * XML databases are a subclass of document-oriented databases that are optimized to work with XML documents.
 * Graph databases are similar, but add another layer, the relationship,
 * which allows them to link documents for rapid traversal.
 */
public interface DocumentEntity {

    /**
     * Creates a {@link DocumentEntity} instance
     *
     * @param name the name of the collection
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when name is null
     */
    static DocumentEntity of(String name) {
        return ServiceLoaderProvider.get(DocumentEntityProvider.class).apply(name);
    }

    /**
     * Creates a {@link DocumentEntity} instance
     *
     * @param name      the collection name
     * @param documents the intial document inside {@link DocumentEntity}
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when either name or documents are null
     */
    static DocumentEntity of(String name, List<Document> documents) {
        DocumentEntity entity = ServiceLoaderProvider.get(DocumentEntityProvider.class).apply(name);
        entity.addAll(documents);
        return entity;
    }

    /**
     * The collection name to {@link DocumentEntity}
     *
     * @return collection name
     */
    String getName();

    /**
     * Remove a Document whose name is informed in parameter.
     *
     * @param documentName a document name
     * @return if a column was removed or not
     * @throws NullPointerException when documentName is null
     */
    boolean remove(String documentName);

    /**
     * List of all documents
     *
     * @return all documents
     */
    List<Document> getDocuments();

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param document a document to be included
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when document is null
     */
    void add(Document document);

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param documentName a name of the document
     * @param value        the information of the document
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String documentName, Object value);

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param documentName a name of the document
     * @param value        the information of the document
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String documentName, Value value);

    /**
     * add all documents within {@link DocumentEntity}
     *
     * @param documents documents to be included
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when document is null
     */
    void addAll(Iterable<Document> documents);

    /**
     * Find document from document name
     *
     * @param documentName a name of a document
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when documentName is null
     */
    Optional<Document> find(String documentName);

    /**
     * Find a document and converts to specific type from {@link Class}.
     * It is a alias to {@link Value#get(Class)}
     *
     * @param documentName a name of a document
     * @param type the type to convert the value
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when there are null parameters
     */
    <T> Optional<T> find(String documentName, Class<T> type);

    /**
     * Find a document and converts to specific type from {@link TypeSupplier}.
     * It is a alias to {@link Value#get(TypeSupplier)}
     *
     * @param documentName a name of a document
     * @param type the type to convert the value
     * @return a new instance converted to informed class
     * @throws NullPointerException when there are null parameters
     */
    <T> Optional<T> find(String documentName, TypeSupplier<T> type);

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int size();

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    boolean isEmpty();

    /**
     * make copy of itself
     *
     * @return an instance copy
     */
    DocumentEntity copy();

    /**
     * Removes all Documents
     */
    void clear();

    /**
     * Returns a Set view of the names of document contained in Document Entity
     *
     * @return the keys
     */
    Set<String> getDocumentNames();

    /**
     * Returns a Collection view of the values contained in this DocumentEntity.
     *
     * @return the collection of values
     */
    Collection<Value> getValues();

    /**
     * Returns true if this DocumentEntity contains a document whose the name is informed
     *
     * @param documentName the document name
     * @return true if find a document and otherwise false
     */
    boolean contains(String documentName);

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link Value#get()} of the map
     *
     * @return a map instance
     */
    Map<String, Object> toMap();

    /**
     * A provider of {@link DocumentEntity} where it will return from the document entity name.
     */
    interface DocumentEntityProvider extends Function<String, DocumentEntity> {
    }
}
