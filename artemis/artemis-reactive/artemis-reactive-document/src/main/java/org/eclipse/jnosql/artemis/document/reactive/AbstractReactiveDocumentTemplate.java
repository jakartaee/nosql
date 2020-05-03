/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.document.reactive;

import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

/**
 * The template method of {@link ReactiveDocumentTemplate}
 */
public abstract class AbstractReactiveDocumentTemplate implements ReactiveDocumentTemplate {

    protected abstract DocumentTemplate getTemplate();

    @Override
    public <T> Publisher<T> insert(T entity) {
        Iterable<T> iterable = () -> Collections.singleton(getTemplate().insert(entity)).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> insert(T entity, Duration ttl) {
        Iterable<T> iterable = () -> Collections.singleton(getTemplate().insert(entity, ttl)).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> insert(Iterable<T> entities) {
        Iterable<T> iterable = () -> getTemplate().insert(entities).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> insert(Iterable<T> entities, Duration ttl) {
        Iterable<T> iterable = () -> getTemplate().insert(entities, ttl).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> update(T entity) {
        Iterable<T> iterable = () -> Collections.singleton(getTemplate().update(entity)).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> update(Iterable<T> entities) {
        Iterable<T> iterable = () -> getTemplate().update(entities).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public Publisher<Void> delete(DocumentDeleteQuery query) {
        Iterable<Void> iterable = () -> {
            getTemplate().delete(query);
            return Collections.<Void>emptyList().iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> select(DocumentQuery query) {
        Iterable<T> iterable = () -> getTemplate().<T>select(query).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> query(String query) {
        Iterable<T> iterable = () -> getTemplate().<T>query(query).iterator();
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> singleResult(String query) {
        Iterable<T> iterable = () -> {
            final Optional<T> result = getTemplate().<T>singleResult(query);
            return result.map(Collections::singleton).orElse(Collections.emptySet()).iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T, K> Publisher<T> find(Class<T> entityClass, K id) {

        Iterable<T> iterable = () -> {
            final Optional<T> result = getTemplate().find(entityClass, id);
            return result.map(Collections::singleton).orElse(Collections.emptySet()).iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T, K> Publisher<Void> delete(Class<T> entityClass, K id) {
        Iterable<Void> iterable = () -> {
            getTemplate().delete(entityClass, id);
            return Collections.<Void>emptyList().iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public Publisher<Long> count(String documentCollection) {
        Iterable<Long> iterable = () -> {
            final long count = getTemplate().count(documentCollection);
            return Collections.singleton(count).iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<Long> count(Class<T> entityType) {
        Iterable<Long> iterable = () -> {
            final long count = getTemplate().count(entityType);
            return Collections.singleton(count).iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }

    @Override
    public <T> Publisher<T> singleResult(DocumentQuery query) {
        Iterable<T> iterable = () -> {
            final Optional<T> result = getTemplate().singleResult(query);
            return result.map(Collections::singleton).orElse(Collections.emptySet()).iterator();
        };
        return ReactiveStreams.fromIterable(iterable).buildRs();
    }
}
