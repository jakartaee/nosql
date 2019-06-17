/*
 * Copyright (c) 2019 Otavio Santana and others
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

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Expresses a dependency to a configuration and its associated persistence unit.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Qualifier
public @interface ConfigurationUnit {


    /**
     * The name of the configuration unit as defined in the settings file.
     *
     * @return the unit name
     */
    @Nonbinding
    String name() default "";

    /**
     * the file name that is within the folder. The default value is jnosql.json
     *
     * @return the file name
     */
    @Nonbinding
    String fileName() default "nosql.json";

    /**
     * It creates both templates and repositories instances using from the respective database.
     *
     * @return The database to templates and repositories classes.
     */
    @Nonbinding
    String database() default "";

    /**
     * Defines a source implementation to the repository. This attribute is used where there are two or more mappers
     * within an application classpath, e.g., mapper-document and mapper-column. Otherwise, it will return an Ambiguous dependency error.
     *
     * @return the repository implementation
     */
    DatabaseType repository() default DatabaseType.SHARED;

    /**
     * @return A qualifier that provides various implementations of a particular repository type.
     * E.g.: when there are several configurations to a specific bean type.
     */
    String qualifier() default "";


}
