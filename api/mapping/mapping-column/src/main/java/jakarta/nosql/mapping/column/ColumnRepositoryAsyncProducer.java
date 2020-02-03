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
package jakarta.nosql.mapping.column;

import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.mapping.RepositoryAsync;

/**
 * The producer of {@link RepositoryAsync}
 *
 */
public interface ColumnRepositoryAsyncProducer {

    /**
     * Produces a Repository class from repository class and {@link ColumnFamilyManagerAsync}
     * @param repositoryClass the repository class
     * @param manager the manager
     * @param <T> the entity of repository
     * @param <K> the K of the entity
     * @param <R> the repository type
     * @return a {@link RepositoryAsync} interface
     * @throws NullPointerException when there is null parameter
     */
    <T, K, R extends RepositoryAsync<T, K>> R get(Class<R> repositoryClass, ColumnFamilyManagerAsync manager);

    /**
     * Produces a Repository class from repository class and {@link ColumnTemplateAsync}
     * @param repositoryClass the repository class
     * @param template the template
     * @param <T> the entity of repository
     * @param <K> the K of the entity
     * @param <R> the repository type
     * @return a {@link RepositoryAsync} interface
     * @throws NullPointerException when there is null parameter
     */
    <T, K, R extends RepositoryAsync<T, K>> R get(Class<R> repositoryClass, ColumnTemplateAsync template);

}