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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Specifies the value of the discriminator column for entities of the given type.
 *
 * <p> The <code>DiscriminatorValue</code>
 * annotation can only be specified on a concrete entity
 * class.
 *
 * <p> If the <code>DiscriminatorValue</code> annotation is not
 * specified and a discriminator column is used, a provider-specific
 * function will be used to generate a value representing the
 * entity type. So the discriminator value default is the {@link Class#getSimpleName()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DiscriminatorValue {
    /**
     * (Optional) The value that indicates that the
     * row is an entity of the annotated entity type.
     */
    String value();
}
