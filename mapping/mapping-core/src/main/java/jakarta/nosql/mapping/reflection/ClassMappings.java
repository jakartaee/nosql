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


import java.util.Optional;

/**
 * This class contains all the class in cached way to be used inside artemis.
 */
public interface ClassMappings {

    /**
     * Find a class in the cached way and return in a class,
     * if it's not found the class will be both, loaded and cached, when this method is called
     *
     * @param classEntity the class of entity
     * @return the {@link ClassMapping}
     * @throws NullPointerException whend class entity is null
     */
    ClassMapping get(Class classEntity);

    /**
     * Returns the {@link ClassMapping} instance from {@link ClassMapping#getName()} in ignore case
     *
     * @param name the name to select ah {@link ClassMapping} instance
     * @return the {@link ClassMapping} from name
     * @throws ClassInformationNotFoundException when the class is not loaded
     * @throws NullPointerException              when the name is null
     */
    ClassMapping findByName(String name);

    /**
     * Returns the {@link ClassMapping} instance from {@link Class#getSimpleName()}
     *
     * @param name the name of {@link Class#getSimpleName()} instance
     * @return the {@link ClassMapping} from name otherwise {@link Optional#empty()}
     * @throws NullPointerException              when the name is null
     */
    Optional<ClassMapping> findBySimpleName(String name);

    /**
     * Returns the {@link ClassMapping} instance from {@link Class#getName()}
     *
     * @param name the name of {@link Class#getName()} instance
     * @return the {@link ClassMapping} from name otherwise {@link Optional#empty()}
     * @throws NullPointerException              when the name is null
     */
    Optional<ClassMapping> findByClassName(String name);

}
