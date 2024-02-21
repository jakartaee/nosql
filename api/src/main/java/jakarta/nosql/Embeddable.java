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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a type whose instances are stored as an intrinsic part of an owning entity, sharing the identity of the entity.
 * A single embeddable type may be used as the type of multiple persistent fields or properties across several entities, even entities of unrelated types.
 *
 * <p>The annotated type must:</p>
 * <ul>
 *     <li>Be a non-abstract, non-final top-level class or static inner class.</li>
 *     <li>Have a public or protected constructor with no parameters or a constructor parameter annotated with {@link Column}.</li>
 * </ul>
 *
 * <p>Enums or interfaces cannot be designated as embeddable types.</p>
 *
 * <p>An embeddable class does not have its own database structure; instead, the state of an instance is stored in the structure mapped by the owning entity.</p>
 *
 * <p>Persistent fields and properties of an embeddable class are mapped using the same mapping annotations used for
 * entity classes and may hold instances of other embeddable types.</p>
 *
 * <p>An embeddable class may not have a field or property annotated with {@link Id}.</p>
 *
 * <p>An embeddable class can have two types of strategies:</p>
 * <ul>
 *     <li>{@link EmbeddableType#FLAT}: where the embeddable class is treated as a parent entity type.</li>
 *     <li>{@link EmbeddableType#GROUPING}: where the embeddable class is treated stored in a structured type.</li>
 * </ul>
 *
 * <p>Example:</p>
 * <pre>{@code
 * &#64;Embeddable
 * public class PhoneNumber {
 *     &#64;Column
 *     protected String areaCode;
 *     &#64;Column
 *     protected String localNumber;
 * }
 * }</pre>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Embeddable {

    /**
     * Specifies the embeddable type.
     * @return the embeddable type
     */
    EmbeddableType value() default EmbeddableType.FLAT;

    /**
     * Defines the strategy for how fields of the embeddable class are stored.
     */
    enum EmbeddableType {
        /**
         * Fields of the embeddable class are embedded directly into the data structure of the parent entity or embeddable.
         */
        FLAT,

        /**
         * Fields of the embeddable class are stored in a structured type, such as a user-defined type (UDT).
         */
        GROUPING
    }
}
