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
package jakarta.nosql.mapping.reflection;

/**
 * It represents the operations within a class such as create a new instance,
 * setter, and getter. These classes are represented with {@link InstanceSupplier}
 * {@link FieldWriter} and {@link FieldReader} respectively.
 */
public interface ClassOperation {

    /**
     * Returns a factory of {@link InstanceSupplier}
     * @return a {@link InstanceSupplier} instance
     */
    InstanceSupplierFactory getInstanceSupplierFactory();

    /**
     * Returns a factory of {@link FieldWriterFactory}
     * @return a {@link FieldWriterFactory}
     */
    FieldWriterFactory getFieldWriterFactory();

    /**
     * Returns a factory of {@link FieldReaderFactory}
     * @return a {@link FieldReaderFactory}
     */
    FieldReaderFactory getFieldReaderFactory();
}

