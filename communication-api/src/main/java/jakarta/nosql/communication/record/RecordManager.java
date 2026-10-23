/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
package jakarta.nosql.communication.record;

import jakarta.nosql.communication.Record;

import java.util.Optional;

/**
 * Defines the minimal communication contract for managing records within
 * a NoSQL database.
 *
 * <p>A {@code RecordManager} provides basic lifecycle operations for
 * interacting with records, including insertion, update, retrieval,
 * and deletion. It is the database-neutral foundation for record-oriented
 * databases such as column, document, graph, time-series, and vector
 * databases.</p>
 *
 * <p>The visibility, durability, consistency, and execution timing of
 * operations defined by this interface are determined by the underlying
 * database implementation.</p>
 *
 * <p>The Communication API is intentionally minimal and does not define
 * query languages, traversal semantics, similarity search, range queries,
 * aggregation models, or other database-specific capabilities. Providers may
 * expose such capabilities through their own extensions.</p>
 *
 * <p>This interface does not define a {@code put} operation, because
 * replace-or-insert semantics cannot be defined consistently across
 * record-oriented databases. Key-value databases define it in
 * {@link jakarta.nosql.communication.keyvalue.KeyValueManager}.</p>
 *
 * <p>Implementations are expected to preserve the native behavior and
 * semantics of the target NoSQL database.</p>
 *
 * @param <T> the managed record type
 */
public interface RecordManager<T extends Record> {

    /**
     * Requests the insertion of a new record into the database.
     *
     * <p>The behavior of this operation, including conflict handling,
     * key generation, validation rules, visibility, durability, and
     * consistency guarantees, is determined by the underlying database
     * implementation.</p>
     *
     * <p>This operation returns the persisted record instance. Provider
     * implementations may return the same instance or a new instance
     * containing additional generated information, such as a database-generated
     * key or provider-specific metadata.</p>
     *
     * <p>Databases that require a key during insertion may reject records
     * without a defined key, typically by throwing a provider-specific
     * exception. Other implementations may generate the key automatically
     * and return a new record instance containing the generated value.</p>
     *
     * <pre>{@code
     * Record record = ...
     *
     * RecordManager manager = ...
     * Record persisted = manager.insert(record);
     * }</pre>
     *
     * @param record the record to insert
     * @return the persisted record instance
     * @throws NullPointerException when the record is {@code null}
     */
    T insert(T record);

    /**
     * Requests the update of an existing record in the database.
     *
     * <p>The behavior of this operation, including update semantics,
     * conflict handling, visibility, durability, consistency guarantees,
     * and execution timing, is determined by the underlying database
     * implementation.</p>
     *
     * <p>This operation returns the updated record instance. Provider
     * implementations may return the same instance or a new instance
     * containing additional generated information or provider-specific
     * metadata.</p>
     *
     * <p>Some NoSQL databases may implement updates using append-only or
     * upsert semantics. In such systems, this operation may behave as an
     * alias for insertion when the target record does not already exist.</p>
     *
     * <p>Other databases may require the record key to exist before the
     * update operation is executed. In these cases, implementations may
     * reject updates for non-existing records, typically by throwing a
     * provider-specific exception.</p>
     *
     * <pre>{@code
     * Record record = ...
     *
     * RecordManager manager = ...
     * Record updated = manager.update(record);
     * }</pre>
     *
     * @param record the record to update
     * @return the updated record instance
     * @throws NullPointerException when the record is {@code null}
     */
    T update(T record);

    /**
     * Requests the insertion of multiple records into the database.
     *
     * <p>The batching behavior, ordering guarantees, visibility,
     * durability, consistency model, partial failure handling,
     * and execution timing are determined by the underlying
     * database implementation.</p>
     *
     * <p>This operation returns the persisted record instances.
     * Provider implementations may return the same instances or
     * new instances containing additional generated information,
     * such as generated keys or provider-specific metadata.</p>
     *
     * <p>Some databases may execute batch operations atomically,
     * while others may partially persist records before a failure
     * occurs. Implementations may also process records lazily,
     * sequentially, or using provider-specific batching strategies.</p>
     *
     * <p>Databases that require keys during insertion may reject
     * records without defined keys, typically by throwing a
     * provider-specific exception. Other implementations may
     * generate keys automatically and return updated record
     * instances containing the generated values.</p>
     *
     * <pre>{@code
     * Iterable<Record> records = ...
     *
     * RecordManager manager = ...
     * Iterable<Record> persisted = manager.insert(records);
     * }</pre>
     *
     * @param records the records to insert
     * @return the persisted record instances
     * @throws NullPointerException when the records are {@code null}
     */
    Iterable<T> insert(Iterable<T> records);


    /**
     * Requests the update of multiple records in the database.
     *
     * <p>The batching behavior, ordering guarantees, visibility,
     * durability, consistency model, partial failure handling,
     * and execution timing are determined by the underlying
     * database implementation.</p>
     *
     * <p>This operation returns the updated record instances.
     * Provider implementations may return the same instances or
     * new instances containing additional generated information
     * or provider-specific metadata.</p>
     *
     * <p>Some NoSQL databases may implement updates using append-only
     * or upsert semantics. In such systems, this operation may behave
     * as an alias for insertion when target records do not already exist.</p>
     *
     * <p>Other databases may require records to exist before updates
     * are executed. In these cases, implementations may reject updates
     * for non-existing records, typically by throwing a provider-specific
     * exception.</p>
     *
     * <p>Some databases may execute batch operations atomically,
     * while others may partially update records before a failure
     * occurs. Implementations may also process records lazily,
     * sequentially, or using provider-specific batching strategies.</p>
     *
     * <pre>{@code
     * Iterable<Record> records = ...
     *
     * RecordManager manager = ...
     * Iterable<Record> updated = manager.update(records);
     * }</pre>
     *
     * @param records the records to update
     * @return the updated record instances
     * @throws NullPointerException when the records are {@code null}
     */
    Iterable<T> update(Iterable<T> records);

    /**
     * Retrieves a record using its unique key.
     *
     * <p>The interpretation, structure, visibility guarantees,
     * consistency model, and lookup semantics of the key are
     * determined by the underlying database implementation.</p>
     *
     * <p>The key may represent different concepts depending on
     * the database model and provider implementation, including
     * identifiers, partition keys, composite keys, node identifiers,
     * or provider-specific key structures.</p>
     *
     * <p>Implementations may perform lookups eagerly, lazily,
     * locally, remotely, or using distributed resolution strategies,
     * according to the characteristics of the underlying database.</p>
     *
     * <p>If no matching record exists, this method returns an
     * empty {@link Optional}.</p>
     *
     * <pre>{@code
     * RecordManager manager = ...
     *
     * Optional<Record> record = manager.findByKey("user:10");
     * }</pre>
     *
     * @param key the unique record key
     * @param <K> the key type
     * @return the matching record when present
     * @throws NullPointerException when the key is {@code null}
     */
    <K> Optional<T> findByKey(K key);

    /**
     * Requests the removal of a record using its unique key.
     *
     * <p>The interpretation, structure, visibility guarantees,
     * consistency model, durability semantics, and execution timing
     * of this operation are determined by the underlying database
     * implementation.</p>
     *
     * <p>The key may represent different concepts depending on
     * the database model and provider implementation, including
     * identifiers, partition keys, composite keys, node identifiers,
     * or provider-specific key structures.</p>
     *
     * <p>Some NoSQL databases may execute deletion operations
     * asynchronously or using eventual consistency strategies.
     * In such systems, record removal may not become immediately
     * visible across all nodes, replicas, or distributed regions.</p>
     *
     * <p>Implementations may reject deletion requests for
     * non-existing records or invalid keys, typically by
     * throwing a provider-specific exception.</p>
     *
     * <pre>{@code
     * RecordManager manager = ...
     *
     * manager.deleteByKey("user:10");
     * }</pre>
     *
     * @param key the unique record key
     * @param <K> the key type
     * @throws NullPointerException when the key is {@code null}
     */
    <K> void deleteByKey(K key);

}
