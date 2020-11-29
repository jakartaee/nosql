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

import jakarta.annotation.Priority;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

final class ServiceLoaderSort<T> implements Comparable<ServiceLoaderSort<T>>, Supplier<T> {

    private final int priority;

    private final T instance;

    private ServiceLoaderSort(int priority, T instance) {
        this.priority = priority;
        this.instance = instance;
    }


    @Override
    public T get() {
        return instance;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(ServiceLoaderSort<T> other) {
        return Integer.compare(other.priority, priority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceLoaderSort<?> that = (ServiceLoaderSort<?>) o;
        return priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(priority);
    }

    static <T> ServiceLoaderSort<T> of(T instance) {
        int priority = Optional.ofNullable(instance.getClass().getAnnotation(Priority.class))
                .map(Priority::value)
                .orElse(0);
        return new ServiceLoaderSort<>(priority, instance);
    }
}
