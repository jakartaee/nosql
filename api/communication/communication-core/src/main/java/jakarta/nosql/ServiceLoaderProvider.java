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
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static java.util.stream.StreamSupport.stream;

import java.lang.reflect.Method;

/**
 * A class that loads class from {@link ServiceLoader}
 */
public final class ServiceLoaderProvider {

    private ServiceLoaderProvider() {
    }
    
    private static final String OSGI_SERVICE_LOADER_CLASS_NAME = "org.glassfish.hk2.osgiresourcelocator.ServiceLoader"; //$NON-NLS-1$

    private static final Map<Class<?>, Object> CACHE = new WeakHashMap<>();
    private static Boolean isOsgi = null;

    private static <T> T getSupplier(Class<T> supplier) {
        requireNonNull(supplier, "supplier is required");

        Object value = CACHE.get(supplier);
        if (value == null) {
            return load(supplier);
        }
        return (T) value;
    }

    /**
     * Searches implementation using {@link ServiceLoader}, and it will return the higher priority
     * {@link javax.annotation.Priority}
     *
     * @param supplier the class
     * @param <T>      the type
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when supplier is null
     */
    public static <T> T get(Class<T> supplier) {
        return getSupplier(requireNonNull(supplier, "supplier is required"));
    }

    /**
     * Searches implementation using {@link ServiceLoader}, an it will return
     * an instance only if there is one implementation to it.
     *
     * @param supplier the class
     * @param <T>      the type
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when supplier is null
     * @throws NonUniqueResultException  where there is more than one result
     */
    public static <T> T getUnique(Class<T> supplier) {
        return getUniqueSupplier(supplier, null);
    }

    /**
     * Searches implementation using {@link ServiceLoader}, an it will return
     * an instance only if there is one implementation to it.
     *
     * @param supplier  the class
     * @param <T>       the type
     * @param predicate the predicate to set in the filter
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when supplier is null
     * @throws NonUniqueResultException  where there is more than one result
     */
    public static <T> T getUnique(Class<T> supplier, Predicate<Object> predicate) {
        return getUniqueSupplier(supplier, predicate);
    }

    /**
     * Searches implementation using {@link ServiceLoader}, an it will return
     * an instance only if there is one implementation to it.
     *
     * @param supplier               the class
     * @param <T>                    the type
     * @param <I>                    a specific implementation of supplier
     * @param supplierImplementation to select a specific implementation
     * @return the instance from the class
     * @throws ProviderNotFoundException when the provider is not found
     * @throws NullPointerException      when supplier is null
     * @throws NonUniqueResultException  where there is more than one result
     */
    public static <T, I extends T> I getUnique(Class<T> supplier, Class<I> supplierImplementation) {
        requireNonNull(supplierImplementation, "supplierImplementation is required");
        Predicate<Object> predicate = p -> p.getClass().equals(supplierImplementation);
        return (I) getUniqueSupplier(supplier, predicate);
    }

    /**
     * Returns an ordered Stream of the supplier
     * @param supplier the supplier
     * @param <T> the supplier type
     * @return the Stream of supplier
     */
    public static <T> Stream<Object> getSupplierStream(Class<T> supplier) {
        if(isOsgi()) {
            try {
                // Use reflection to avoid having any dependency on HK2 ServiceLoader class
                Class<?>[] args = new Class<?>[]{supplier};
                Class<?> target = Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME);
                Method m = target.getMethod("lookupProviderInstances", Class.class); //$NON-NLS-1$
                @SuppressWarnings("unchecked")
                Spliterator<Object> iter = (Spliterator<Object>)((Iterable<?>) m.invoke(null, (Object[]) args)).spliterator();
                return StreamSupport.stream(iter, false);
            } catch (Exception ignored) {
                // Fall through to non-OSGi behavior
            }
        }
        return stream(ServiceLoader.load(supplier).spliterator(), false)
                .map(ServiceLoaderSort::of)
                .sorted()
                .map(ServiceLoaderSort::get);
    }

    private static <T> T getUniqueSupplier(Class<T> supplier, Predicate<Object> predicate) {
        Stream<Object> stream = getSupplierStream(requireNonNull(supplier, "supplier is required"));
        if (predicate != null) {
            stream = stream.filter(predicate);
        }
        final List<Object> suppliers = stream.collect(Collectors.toList());
        if (suppliers.size() == 1) {
            return (T) suppliers.get(0);
        } else if (suppliers.isEmpty()) {
            throw new ProviderNotFoundException(supplier);
        }
        throw new NonUniqueResultException("There is more than one supplier of the type: " + supplier);
    }


    private static <T> T load(Class<T> supplier) {
        synchronized (supplier) {
            Object result = getSupplierStream(supplier)
                    .findFirst().orElseThrow(() -> new ProviderNotFoundException(supplier));
            CACHE.put(supplier, result);
            return (T) result;
        }
    }
    
    private static synchronized boolean isOsgi() {
        if(isOsgi == null) {
            try {
                Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME);
                isOsgi = true;
            } catch (ClassNotFoundException ignored) {
                isOsgi = false;
            }
        }
        return isOsgi;
    }

}