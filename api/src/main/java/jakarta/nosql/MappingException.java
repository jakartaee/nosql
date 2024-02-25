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

/**
 * An exception that occurs when there is a mapping error during entity mapping or persistence.
 * This exception typically indicates issues with the usage or missing annotations at the entity level.
 * Some scenarios where this exception may be thrown include:
 * <ul>
 *     <li>A class annotated with {@code @Entity} does not have the {@code @Id} annotation.</li>
 *     <li>A class annotated with {@code @Entity} or {@code @Embeddable} does not have a default constructor
 *     or has multiple constructors without {@code @Id} and {@code @Column} annotations.</li>
 *     <li>A class annotated with {@code @Entity} or {@code @Embeddable} has multiple constructors with annotations
 *     provided by Jakarta NoSQL, which may lead to ambiguity in entity instantiation.</li>
 * </ul>
 *
 * <p>This exception may occur at runtime or build time, depending on the Jakarta NoSQL provider being used.</p>
 */
public class MappingException extends NoSQLException {

    /**
     * Constructs a {@code MappingException} using the given message and cause.
     *
     * @param message A message explaining the exception condition
     * @param cause   The underlying cause
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@code MappingException} using the given cause.
     *
     * @param cause The underlying cause
     */
    public MappingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a {@code MappingException} using the given message.
     *
     * @param message A message explaining the exception condition
     */
    public MappingException(String message) {
        super(message);
    }
}
