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
package jakarta.nosql.mapping.reflection;

import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This instance is the meta-info of a loaded class that used to be annotated with {@link Entity}.
 */
public interface ClassMapping {


    /**
     * @return the Entity name
     */
    String getName();

    /**
     * @return the fields name
     */
    List<String> getFieldsName();

    /**
     * @return The class
     */
    Class<?> getClassInstance();

    /**
     * @return The fields from this class
     */
    List<FieldMapping> getFields();


    /**
     * Creates a new instance from {@link InstanceSupplier}
     * @param <T> the instance type
     * @return a new instance of this class
     */
    <T> T newInstance();


    /**
     * Gets the native column name from the Java field name
     *
     * @param javaField the java field
     * @return the column name or column
     * @throws NullPointerException when javaField is null
     */
    String getColumnField(String javaField);

    /**
     * Gets the {@link FieldMapping} from the java field name
     *
     * @param javaField the java field
     * @return the field otherwise {@link Optional#empty()}
     * @throws NullPointerException when the javaField is null
     */
    Optional<FieldMapping> getFieldMapping(String javaField);

    /**
     * Returns a Fields grouped by the name
     *
     * @return a {@link FieldMapping} grouped by
     * {@link FieldMapping#getName()}
     * @see FieldMapping#getName()
     */
    Map<String, FieldMapping> getFieldsGroupByName();


    /**
     * Returns the field that has {@link Id} annotation
     *
     * @return the field with ID annotation
     */
    Optional<FieldMapping> getId();
}
