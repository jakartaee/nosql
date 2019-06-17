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
 * A piece of operations within a class.
 * This class does the getter operation in a {@link java.lang.reflect.Field} in a class from a Field.
 */
public interface FieldReader {

    /**
     * Reads the entity bean, it will read the respective field and return the value.
     *
     * @param bean the entity that has the field
     * @return the field value from the entity
     * @throws NullPointerException when bean is null
     */
    Object read(Object bean);
}
