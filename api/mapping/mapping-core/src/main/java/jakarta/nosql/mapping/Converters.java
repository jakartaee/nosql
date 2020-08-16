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

/**
 * The Converter storage
 */
public interface Converters {

    /**
     * Return a new instance from the converter
     *
     * @param converterClass the converter class
     * @param <X> the type of the entity attribute
     * @param <Y> the type of the database column
     * @return a converter instance
     * @throws NullPointerException when converter is null
     */
    <X, Y> AttributeConverter<X, Y> get(Class<? extends AttributeConverter<X, Y>> converterClass);
}
