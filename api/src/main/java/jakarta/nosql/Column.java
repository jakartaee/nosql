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
 * Declares that the annotated attribute is persistable and maps to a database column or key.
 *
 * <p>This annotation is required to make fields persistable in Jakarta NoSQL.
 * Only attributes explicitly annotated with {@code @Column} will be included in persistence operations.</p>
 *
 * <p>If the {@code name} attribute is omitted, the Java attribute name is used as the default mapping target.</p>
 *
 * <p>Example with a custom column name:</p>
 * <pre>{@code
 * @Entity
 * public class Product {
 *
 *     @Column(name = "DESC")
 *     private String description;
 * }
 * }</pre>
 *
 * <p><strong>Note:</strong> {@code @Column} is also used in {@link Projection} classes for mapping query results.
 * In that context, it is used for read-only access and supports mapping of nested or aliased fields.</p>
 *
 * @see Entity
 * @see MappedSuperclass
 * @see Embeddable
 * @see Projection
 * @see Convert
 * @see Id
 * @since 1.0.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Column {
    /**
     * (Optional) The name of the column. Defaults to the property or field name.
     * <p>
     * For example, in the {@code Person} entity class below, the mapped fields with {@code @Column}
     * will be mapped to columns with their respective field name:
     * </p>
     * <pre>{@code
     * @Entity
     * public class Person {
     *     @Column
     *     private String name;
     * }
     * }</pre>
     * <p>
     * If any name customization is needed, set the value of this attribute to specify the desired name.
     * For instance, in the example below, the {@code name} field of the {@code Person} class will be mapped
     * to the "personName" column:
     * </p>
     * <pre>{@code
     * @Entity
     * public class Person {
     *     @Column("personName")
     *     private String name;
     * }
     * }</pre>
     *
     * @return the column name
     */
    String value() default "";

    /**
     * (Optional) Defines the name of the user-defined type (UDT) used by the NoSQL database for this field.
     * <p>
     * If a NoSQL database supports UDTs, this attribute allows specifying the name of the UDT to be used
     * for serializing this field. If the database does not support UDTs, this field will be skipped during serialization.
     * </p>
     *
     * @return the user-defined type (UDT) name
     */
    String udt() default "";
}
