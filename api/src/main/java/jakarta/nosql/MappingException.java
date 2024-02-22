/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

public class MappingException extends NoSQLException {

    /**
     * Constructs a {@code MappingException} using the given information.
     *
     * @param message A message explaining the exception condition
     * @param cause The underlying cause
     */
    public MappingException(String message, Throwable cause) {
        super( message, cause );
    }

    /**
     * Constructs a {@code MappingException} using the given information.
     *
     * @param cause The underlying cause
     */
    public MappingException(Throwable cause) {
        super( cause );
    }

    /**
     * Constructs a {@code MappingException} using the given information.
     *
     * @param message A message explaining the exception condition
     */
    public MappingException(String message) {
        super( message );
    }
}
