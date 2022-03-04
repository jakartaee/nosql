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


import java.util.ArrayList;
import java.util.List;

/**
 * Decorators of all {@link TypeReferenceReader} supported by Jakarta NoSQL
 *
 * @see ValueReader
 */
public final class TypeReferenceReaderDecorator implements TypeReferenceReader {

    private static final TypeReferenceReaderDecorator INSTANCE = new TypeReferenceReaderDecorator();

    private final List<TypeReferenceReader> readers = new ArrayList<>();

    {
        ServiceLoaderProvider.getSupplierStream(TypeReferenceReader.class)
            .map(TypeReferenceReader.class::cast)
            .forEach(readers::add);
    }

    public static TypeReferenceReaderDecorator getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean test(TypeSupplier type) {
        return readers.stream().anyMatch(r -> r.test(type));
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object value) {

        TypeReferenceReader valueReader = readers.stream().filter(r -> r.test(typeReference)).findFirst().
                orElseThrow(() -> new UnsupportedOperationException("The type " + typeReference + " is not supported yet"));
        return valueReader.convert(typeReference, value);
    }

    @Override
    public String toString() {
        return  "TypeReferenceReaderDecorator{" + "readers=" + readers +
                '}';
    }


}
