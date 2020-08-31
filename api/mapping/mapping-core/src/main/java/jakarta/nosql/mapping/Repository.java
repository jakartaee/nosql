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


import java.util.Optional;

/**
 * <p>Interface to generic CRUD operations on a repository for a specific type.</p>
 * <p>The interface that extends need to be recovered by CDI, so either the bean-discovery-mode="all" be set in bean.xml
 * or define a scope in the interface.</p>
 * <p><a href="https://docs.jboss.org/cdi/spec/2.0/cdi-spec.html#default_bean_discovery">https://docs.jboss.org/cdi/spec/2.0/cdi-spec.html#default_bean_discovery</a></p>
 * The query builder mechanism built into Jakarta NoSQL Mapping repository infrastructure is useful for building constraining queries
 * over entities of the repository. The mechanism strips the prefixes is defined by:
 * <p>findBy: to select any information T</p>
 * <p>deleteBy: To delete any information T</p>
 * Jakarta NoSQL Mapping has some keywords on method:
 * <p><b>And</b></p>
 * <p><b>Or</b></p>
 * <p><b>Between</b></p>
 * <p><b>LessThan</b></p>
 * <p><b>GreaterThan</b></p>
 * <p><b>LessThanEqual</b></p>
 * <p><b>GreaterThanEqual</b></p>
 * <p><b>Like</b></p>
 * <p><b>In</b></p>
 * <p><b>OrderBy</b></p>
 * <p><b>OrderBy____Desc</b></p>
 * <p><b>OrderBy_____ASC</b></p>
 *
 * @param <T>  the bean type
 * @param <K> the K type
 */
public interface Repository<T, K> {

    /**
     * Saves entity
     *
     * @param <S>    the entity type
     * @param entity entity to be saved
     * @return the entity saved
     * @throws NullPointerException when document is null
     */
    <S extends T> S save(S entity);

    /**
     * Saves entity
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param <S>      the entity type
     * @param entities entities to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    <S extends T> Iterable<S> save(Iterable<S> entities);

    /**
     * Deletes the entity with the given id.
     *
     * @param id the id
     * @throws NullPointerException when id is null
     */
    void deleteById(K id);

    /**
     * Deletes the entity with the given ids.
     *
     * @param ids the ids
     * @throws NullPointerException when either ids or same element is null
     */
    void deleteById(Iterable<K> ids);

    /**
     * Finds an entity given the id
     *
     * @param id the id
     * @return the entity given the K
     * @throws NullPointerException when id is null
     */
    Optional<T> findById(K id);

    /**
     * Finds the entities given ids
     *
     * @param ids the ids
     * @return the entities from ids
     * @throws NullPointerException when the id is null
     */
    Iterable<T> findById(Iterable<K> ids);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id the id
     * @return if the entity does exist or not
     * @throws NullPointerException when id is null
     */
    boolean existsById(K id);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    long count();


}
