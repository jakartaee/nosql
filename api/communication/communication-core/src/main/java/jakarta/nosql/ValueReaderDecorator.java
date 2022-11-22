/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link ValueReader} supported by Jakarta NoSQL
 * @see ValueReader
 */
public final class ValueReaderDecorator implements ValueReader {

    private static final ValueReaderDecorator INSTANCE = new ValueReaderDecorator();

    private final List<ValueReader> readers = new ArrayList<>();

    {
        ServiceLoaderProvider.getSupplierStream(ValueReader.class,
                        () -> ServiceLoader.load(ValueReader.class))
            .map(ValueReader.class::cast)
            .forEach(readers::add);
    }

    public static ValueReaderDecorator getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean test(Class type) {
        return readers.stream().anyMatch(r -> r.test(type));
    }

    @Override
    public <T> T read(Class<T> type, Object value) {
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        ValueReader valueReader = readers.stream().filter(r -> r.test(type)).findFirst().orElseThrow(
            () -> new UnsupportedOperationException("The type " + type + " is not supported yet"));
        return valueReader.read(type, value);
    }

    @Override
    public String toString() {
        return  "ValueReaderDecorator{" + "readers=" + readers +
                '}';
    }


}
