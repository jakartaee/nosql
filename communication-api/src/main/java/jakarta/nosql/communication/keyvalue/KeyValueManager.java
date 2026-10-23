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
package jakarta.nosql.communication.keyvalue;

import jakarta.nosql.communication.record.RecordManager;

import java.time.Duration;
import java.util.Optional;

/**
 * Defines the communication contract for interacting with
 * key-value databases.
 *
 * <p>A {@code KeyValueManager} specializes {@link RecordManager}
 * for {@link KeyValueRecord} instances, providing lifecycle
 * operations aligned with key-value storage semantics.</p>
 *
 * <p>This manager preserves the minimal and flexible nature of
 * key-value systems, where data is represented as the association
 * between a unique key and a corresponding value.</p>
 *
 * <p>The visibility, durability, consistency guarantees,
 * replication model, batching behavior, and execution semantics
 * of operations are determined by the underlying database
 * implementation.</p>
 *
 * <p>Implementations may support additional capabilities such as
 * expiration policies, append-only storage, distributed replication,
 * provider-specific serialization strategies, or eventual consistency
 * models.</p>
 */
public interface KeyValueManager extends RecordManager<KeyValueRecord> {

    /**
     * Returns the bucket associated with this manager when supported
     * by the underlying database implementation.
     *
     * <p>Some key-value databases expose buckets, collections,
     * namespaces, or equivalent logical grouping concepts as
     * first-class structures. Other implementations may infer
     * bucket information from configuration, key conventions,
     * provider-specific strategies, or may not expose bucket
     * concepts at all.</p>
     *
     * <p>The interpretation and semantics of buckets are determined
     * by the underlying database implementation.</p>
     *
     * <pre>{@code
     * KeyValueManager manager = ...
     *
     * manager.bucket()
     *         .ifPresent(System.out::println);
     * }</pre>
     *
     * @return the bucket associated with this manager when available
     */
    Optional<String> bucket();

    /**
     * Stores a record in the key-value database.
     *
     * <p>This operation behaves as a complete replacement operation.
     * When the record key does not exist, the operation behaves as
     * an insertion. When the key already exists, the existing value
     * is replaced by the provided record.</p>
     *
     * <p>The replacement semantics, visibility guarantees,
     * durability model, consistency guarantees, and execution timing
     * are determined by the underlying database implementation.</p>
     *
     * <p>Some databases may execute this operation asynchronously
     * or using eventual consistency strategies. In such systems,
     * updates may not become immediately visible across all nodes,
     * replicas, or distributed regions.</p>
     *
     * <p>This operation returns the stored record instance.
     * Implementations may return the same instance or a new
     * instance containing additional generated information
     * or provider-specific metadata.</p>
     *
     * <pre>{@code
     * KeyValueRecord record = ...
     *
     * KeyValueManager manager = ...
     * KeyValueRecord stored = manager.put(record);
     * }</pre>
     *
     * @param record the record to store
     * @return the stored record instance
     * @throws NullPointerException when the record is {@code null}
     */
    KeyValueRecord put(KeyValueRecord record);


    /**
     * Stores a record in the key-value database using a
     * time-to-live expiration policy.
     *
     * <p>This operation behaves as a complete replacement operation.
     * When the record key does not exist, the operation behaves as
     * an insertion. When the key already exists, the existing value
     * is replaced by the provided record.</p>
     *
     * <p>The provided duration defines the maximum lifetime of the
     * stored record according to the semantics of the underlying
     * database implementation.</p>
     *
     * <p>The expiration behavior, visibility guarantees,
     * durability model, consistency guarantees, and execution timing
     * are determined by the underlying database implementation.</p>
     *
     * <p>Some databases may execute this operation asynchronously
     * or using eventual consistency strategies. In such systems,
     * updates and expiration events may not become immediately
     * visible across all nodes, replicas, or distributed regions.</p>
     *
     * <p>Databases that do not support expiration or time-to-live
     * policies may reject this operation by throwing
     * {@link UnsupportedOperationException}.</p>
     *
     * <pre>{@code
     * KeyValueRecord record = ...
     *
     * KeyValueManager manager = ...
     *
     * KeyValueRecord stored =
     *         manager.put(record, Duration.ofMinutes(10));
     * }</pre>
     *
     * @param record the record to store
     * @param duration the record lifetime
     * @return the stored record instance
     * @throws NullPointerException when the record or duration is {@code null}
     * @throws UnsupportedOperationException when the database does not support TTL
     */
    KeyValueRecord put(KeyValueRecord record, Duration duration);
}
