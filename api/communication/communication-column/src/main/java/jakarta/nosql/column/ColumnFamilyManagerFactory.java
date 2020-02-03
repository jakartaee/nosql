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

package jakarta.nosql.column;

/**
 * {@link ColumnFamilyManager} factory.
 * When the application has finished using the column family manager factory, and/or at application shutdown,
 * the application should close the column family manager factory.
 */
public interface ColumnFamilyManagerFactory extends AutoCloseable {

    /**
     * Creates a {@link ColumnFamilyManager} from database's name
     *
     * @param <T>      the {@link ColumnFamilyManager} type
     * @param database a database name
     * @return a new {@link ColumnFamilyManager} instance
     * @throws UnsupportedOperationException when this operation is not supported
     *                                       throws {@link NullPointerException} when the database is null
     */
    <T extends ColumnFamilyManager> T get(String database);

    /**
     * closes a resource
     */
    void close();
}
