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
 * Specifies the mapped field of an entity is the entity’s ID, or the Key in Key-Value databases.
 *
 * @see Entity
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Id {

    /**
     * The name of the entity ID. Default value is {@code _id}.
     *
     * <pre>{@code
     * @Entity
     * public class User {
     *
     *     @Id
     *     private String userName;
     *
     * }
     * }</pre>
     * <p>
     * if the entity ID name requires customization, it just set the single attribute of the annotation to specify the desired name:
     *
     * <pre>{@code
     * @Entity
     * public class User {
     *
     *     @Id("userId")
     *     private String userName;
     *
     * }
     * }</pre>
     * <p>
     * @return the entity ID name
     */
    String value() default "_id";
}
