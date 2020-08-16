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

import jakarta.nosql.Value;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * This class has utilitarian class to {@link Document}
 */
public final class Documents {


    private static final Predicate<Map.Entry<String, ?>> IS_VALUE_NULL = e -> Objects.nonNull(e.getValue());

    private Documents() {
    }

    /**
     * An alias to {@link Document#of(String, Object)}
     *
     * @param name  the name
     * @param value the value
     * @return the document instance
     */
    public static Document of(String name, Object value) {
        return Document.of(name, Value.of(value));
    }


    /**
     * Converts the map to {@link List} of {@link Document}
     *
     * @param values the map
     * @return the list instance
     * @throws NullPointerException when map is null
     */
    public static List<Document> of(Map<String, ?> values) {
        Objects.requireNonNull(values, "values is required");

        return values.entrySet().stream()
                .filter(IS_VALUE_NULL)
                .map(e -> Document.of(e.getKey(), getValue(e.getValue())))
                .collect(toList());
    }

    private static Object getValue(Object value) {

        if (value instanceof Map<?, ?>) {
            List<?> list = Documents.of((Map.class.cast(value)));
            if (list.size() == 1) {
                return list.get(0);
            }
            return list;
        }
        if (value instanceof Iterable<?>) {
            return stream(Iterable.class.cast(value).spliterator(), false)
                    .map(Documents::getValue).collect(toList());
        }
        return value;
    }

}
