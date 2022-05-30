/*
 * Copyright (c) 2022 Otavio Santana and others
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

import java.lang.reflect.Method;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.StreamSupport.stream;

enum LoaderType {
    OSGI {
        <T> Stream<Object> read(Class<T> supplier) {
            try {
                // Use reflection to avoid having any dependency on HK2 ServiceLoader class
                Class<?>[] args = new Class<?>[]{supplier};
                Class<?> target = Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME);
                Method m = target.getMethod("lookupProviderInstances", Class.class); //$NON-NLS-1$
                @SuppressWarnings("unchecked")
                Iterable<Object> instances = (Iterable<Object>) m.invoke(null, (Object[]) args);
                if (instances != null) {
                    return StreamSupport.stream(instances.spliterator(), false)
                            .map(ServiceLoaderSort::of)
                            .sorted()
                            .map(ServiceLoaderSort::get);
                }
            } catch (Exception ignored) {
                // Fall through to non-OSGi behavior
                return SERVICE_LOADER.read(supplier);
            }
            return null;
        }
    }, SERVICE_LOADER {
        <T> Stream<Object> read(Class<T> supplier) {
            return stream(ServiceLoader.load(supplier).spliterator(), false)
                    .map(ServiceLoaderSort::of)
                    .sorted()
                    .map(ServiceLoaderSort::get);
        }
    };

    abstract <T> Stream<Object> read(Class<T> supplier);
    private static final String OSGI_SERVICE_LOADER_CLASS_NAME = "org.glassfish.hk2.osgiresourcelocator.ServiceLoader"; //$NON-NLS-1$

}
