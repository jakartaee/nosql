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


/**
 * {@link DocumentCollectionManager} factory.
 * When the application has finished using the document collection manager factory, and/or at application shutdown,
 * the application should close the column family manager factory.
 */
public interface DocumentCollectionManagerAsyncFactory extends AutoCloseable {

    /**
     * Creates a {@link DocumentCollectionManagerAsync} from database's name
     *
     * @param database a database name
     * @param <T>      {@link DocumentCollectionManagerAsync} type
     * @return a new {@link DocumentCollectionManagerAsync} instance
     * @throws UnsupportedOperationException when this operation is not supported
     * @throws NullPointerException          when the database is null
     */
    <T extends DocumentCollectionManagerAsync> T getAsync(String database);

    /**
     * closes a resource
     */
    void close();
}
