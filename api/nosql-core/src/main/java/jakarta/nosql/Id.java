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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the primary key of an entity.
 * Specifies the mapped field of an entity as the entity’s ID, or the Key in Key-Value databases.
 * <p>
 * The field or property to which the {@code Id} annotation is
 * applied should have one of the following types:
 * <ul>
 * <li>any Java primitive type;</li>
 * <li>any primitive wrapper type;</li>
 * <li>{@link String};</li>
 * <li>{@link java.util.UUID};</li>
 * <li>{@link java.math.BigDecimal};</li>
 * <li>{@link java.math.BigInteger}.</li>
 * </ul>
 * Or it uses the {@link Converter} to convert the type to one of the above types.
 * </p>
 * <p>
 * The mapped column for the primary key of the entity is assumed
 * to be the primary key of the database structure.
 * </p>
 *
 * <p>Example:
 * {@snippet :
 * @Id
 * public Long id;
 * }
 * An insertion with an ID value might vary from the vendor, where vendors can throw a NullPointerException,
 * for example, a Key-value database, or apply a strategy of generating value, for example, UUID.
 * </p>
 *
 * @see Column
 * @see Entity
 * @see Converter
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Id {

    /**
     * The name of the entity ID. Default value is {@code _id}.
     * This value might be ignored if the NoSQL database has it keyword reserved for keys.
     * For example:
     * {@code
     * @Entity
     * public class User {
     *     @Id
     *     private String userName;
     * }
     * }
     * @return the entity ID name
     */
    String value() default "_id";
}
