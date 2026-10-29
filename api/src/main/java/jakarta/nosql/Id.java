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
 * The identifier type should be one of the basic types defined by the Jakarta NoSQL specification, or
 * a type converted through {@link Convert}. Supported identifier types and any additional constraints
 * depend on the database model and the provider. For example, a time-series provider may require a temporal
 * identifier using a supported Java date/time type, and a vector provider may impose its own restrictions.
 * No particular identifier type is universally required.
 * </p>
 * <p>
 * The mapped column for the primary key of the entity is assumed
 * to be the primary key of the database structure.
 * </p>
 *
 * <p>Example:</p>
 * <pre>
 * {@code
 * @Id
 * public Long id;
 * }
 * </pre>
 * <p>
 * An insertion with an ID value might vary from the vendor, where vendors can throw a NullPointerException,
 * for example, a Key-value database, or apply a strategy of auto generating value, for example, UUID or auto-increment.
 * </p>
 *
 * @see Convert
 * @see Column
 * @see Entity
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Id {

    /**
     * The name of the entity ID. Default value is {@code _id}.
     * <p>
     * By default, the field marked with {@code @Id} maps to the field {@code _id} in the database. However,
     * this can vary depending on the NoSQL provider. Certain databases may use a different field name for
     * the identifier, such as {@code _key}, or allow users to override this value based on the specific
     * database implementation. It is recommended to consult the documentation of the database provider
     * to understand its requirements for primary key fields.
     * </p>
     * <p>Example of customization:</p>
     * <pre>
     * {@code
     * @Entity
     * public class User {
     *     @Id("userId")
     *     private String userName;
     * }
     * }
     * </pre>
     *
     * @return the entity ID name
     */
    String value() default "_id";
}
