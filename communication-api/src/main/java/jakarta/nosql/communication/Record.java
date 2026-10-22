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
package jakarta.nosql.communication;

/**
 * Represents the minimal communication structure that can be persisted
 * within a NoSQL database.
 *
 * <p>A {@code Record} defines the fundamental unit of interaction used by
 * the Communication API. It provides access to the record key, which uniquely
 * identifies the data within the underlying database.</p>
 *
 * <p>This abstraction is intentionally minimal and does not impose assumptions
 * about the internal structure, schema, or semantics of the stored data.
 * It is shared by the key-value and record communication models, and
 * providers may implement their own record representations.</p>
 *
 * <p>The type and semantics of the key are determined by the underlying
 * database and provider implementation.</p>
 */
public interface Record {


    /**
     * Returns the unique key associated with this record.
     *
     * @param <K> the key type
     * @return the record key
     */
    <K> K  key();

}
