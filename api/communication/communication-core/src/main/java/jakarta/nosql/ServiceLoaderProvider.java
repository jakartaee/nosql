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
package jakarta.nosql;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A class that loads class from {@link ServiceLoader}
 */
public final class ServiceLoaderProvider {
    private ServiceLoaderProvider() {
    }

    private static final Map<Class<?>, Object> CACHE = new WeakHashMap<>();
    private static final LoaderType LOADER_TYPE = LoaderType.getLoaderType();

    /**
     * Searches implementation using {@link ServiceLoader}, and it will return the higher priority
     * {@link javax.annotation.Priority}
     *
     * @param service  the class
     * @param supplier the ServiceLoader supplier
     * @param <T>      the type
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when service is null
     */
    public static <T> T get(Class<T> service, Supplier<ServiceLoader<T>> supplier) {
        requireNonNull(service, "service is required");
        requireNonNull(supplier, "supplier is required");
        return getSupplier(service, supplier);
    }

    /**
     * Searches implementation using {@link ServiceLoader}, it will return
     * an instance only if there is one implementation to it.
     *
     * @param service  the class
     * @param <T>      the type
     * @param supplier the ServiceLoader supplier
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when service is null
     * @throws NonUniqueResultException  where there is more than one result
     */
    public static <T> T getUnique(Class<T> service, Supplier<ServiceLoader<T>> supplier) {
        return getUniqueSupplier(service, supplier, null);
    }

    /**
     * Searches implementation using {@link ServiceLoader}, it will return
     * an instance only if there is one implementation to it.
     *
     * @param service   the class
     * @param supplier  a supplier to service loader
     * @param <T>       the type
     * @param predicate the predicate to set in the filter
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when service is null
     * @throws NonUniqueResultException  where there is more than one result
     */
    public static <T> T getUnique(Class<T> service, Supplier<ServiceLoader<T>> supplier, Predicate<Object> predicate) {
        return getUniqueSupplier(service, supplier, predicate);
    }

    /**
     * Searches implementation using {@link ServiceLoader}, it will return
     * an instance only if there is one implementation to it.
     *
     * @param service        the class
     * @param supplier       the ServiceLoader supplier
     * @param <T>            the type
     * @param <I>            a specific implementation of service
     * @param implementation to select a specific implementation
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when service is null
     * @throws NonUniqueResultException  where there is more than one result
     */
    public static <T, I extends T> I getUnique(Class<T> service, Supplier<ServiceLoader<T>> supplier,
                                               Class<I> implementation) {
        requireNonNull(implementation, "implementation is required");
        Predicate<Object> predicate = p -> p.getClass().equals(implementation);
        return (I) getUniqueSupplier(service, supplier, predicate);
    }

    /**
     * Returns an ordered Stream of the service
     *
     * @param service  the service
     * @param supplier the ServiceLoader supplier
     * @param <T>      the service type
     * @return the Stream of service
     * @throws NullPointerException when there is null parameter
     */
    public static <T> Stream<Object> getSupplierStream(Class<T> service, Supplier<ServiceLoader<T>> supplier) {
        Objects.requireNonNull(service, "service is required");
        Objects.requireNonNull(supplier, "supplier is required");

        return LOADER_TYPE.read(service, supplier);
    }

    private static <T> T getSupplier(Class<T> service, Supplier<ServiceLoader<T>> supplier) {

        Object value = CACHE.get(service);
        if (value == null) {
            return load(service, supplier);
        }
        return (T) value;
    }

    private static <T> T getUniqueSupplier(Class<T> service, Supplier<ServiceLoader<T>> supplier, Predicate<Object> predicate) {
        requireNonNull(service, "service is required");
        requireNonNull(supplier, "supplier is required");
        Stream<Object> stream = getSupplierStream(service, supplier);
        if (predicate != null) {
            stream = stream.filter(predicate);
        }
        final List<Object> suppliers = stream.collect(Collectors.toList());
        if (suppliers.size() == 1) {
            return (T) suppliers.get(0);
        } else if (suppliers.isEmpty()) {
            throw new ProviderNotFoundException(service);
        }
        throw new NonUniqueResultException("There is more than one service of the type: " + service);
    }

    private static <T> T load(Class<T> service, Supplier<ServiceLoader<T>> supplier) {
        synchronized (service) {
            Object result = getSupplierStream(service, supplier)
                    .findFirst().orElseThrow(() -> new ProviderNotFoundException(service));
            CACHE.put(service, result);
            return (T) result;
        }
    }

}