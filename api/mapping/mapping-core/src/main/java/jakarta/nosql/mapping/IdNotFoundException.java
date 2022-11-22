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
package jakarta.nosql.mapping;

import java.util.function.Supplier;

/**
 * When The Entity is converted to communication layer,
 * this entity must have a field with {@link Id} annotation. If this entity
 * hasn't this information an exception will be launch.
 */
public class IdNotFoundException extends MappingException {

    public static final Supplier<IdNotFoundException> KEY_NOT_FOUND_EXCEPTION_SUPPLIER = ()
            -> new IdNotFoundException("To use this resource you must annotated a field with @Id");
    /**
     * New exception instance with the exception message
     *
     * @param message the exception message
     */
    public IdNotFoundException(String message) {
        super(message);
    }


    public static IdNotFoundException newInstance(Class<?> type) {
        String message = "The entity " + type + " must have a field annotated with @Id";
        return new IdNotFoundException(message);
    }
}
