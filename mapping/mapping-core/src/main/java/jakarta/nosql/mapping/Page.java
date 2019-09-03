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
package jakarta.nosql.mapping;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A page is a fixed-length contiguous block of entities from the database.
 *
 * @param <T> the entity type
 */
public interface Page<T> extends Supplier<Stream<T>> {

    /**
     * Returns the {@link Pagination} of the current {@link Page}
     *
     * @return a current {@link Pagination}
     */
    Pagination getPagination();

    /**
     * Returns the {@link Page} requesting the next {@link Page}.
     *
     * @return the next {@link Page}
     */
    Page<T> next();

    /**
     * Returns the page content as {@link Stream}
     *
     * @return the content as {@link Stream}
     */
    Stream<T> getContent();

    /**
     * Returns the page content as from collection Factory
     *
     * @param collectionFactory the collectionFactory
     * @param <C>               the collection type
     * @return a content of this page as {@link Collection} from collectionFactory
     * @throws NullPointerException when collectionFactory is null
     */
    <C extends Collection<T>> C getContent(Supplier<C> collectionFactory);

}
