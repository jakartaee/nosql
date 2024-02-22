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
 * Specifies the discriminator column for the mapping strategy.
 * If the <code>DiscriminatorColumn</code> annotation is missing,
 * the name of the discriminator column defaults to <code>"dtype"</code>.
 *
 * <p>Example usage:
 * <pre>{@code
 * @Entity
 * @DiscriminatorColumn(name = "dtype")
 * public class Animal {
 *     // Common fields and methods for all animals
 * }
 *
 * @Entity
 * public class Dog extends Animal {
 *     // Specific fields and methods for dogs
 * }
 *
 * @Entity
 * public class Cat extends Animal {
 *     // Specific fields and methods for cats
 * }
 * }</pre>
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DiscriminatorColumn {

    /**
     * The default name of the discriminator column, which is {@code "dtype"}.
     */
    String DEFAULT_DISCRIMINATOR_COLUMN = "dtype";

    /**
     * (Optional) The name of the column to be used for the discriminator.
     *
     * @return the column's name
     */
    String value() default DEFAULT_DISCRIMINATOR_COLUMN;
}

