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

import jakarta.nosql.Settings;

import java.util.Optional;

/**
 * This interface represents the Configuration Unit information.
 */
public interface ConfigurationSettingsUnit {

    /**
     * Returns the unit-name from the configuration.
     * When a configuration file has more than one, the injection configuration unit must match {@link ConfigurationUnit#name()}
     *
     * @return the name otherwise {@link Optional#empty()}
     * @see ConfigurationUnit
     */
    Optional<String> getName();

    /**
     * Returns the description
     *
     * @return the description otherwise {@link Optional#empty()}
     */
    Optional<String> getDescription();

    /**
     * Returns the class provider
     *
     * @param <T> the class type
     * @return the provider class otherwise {@link Optional#empty()}
     */
    <T> Optional<Class<T>> getProvider();

    /**
     * Returns the settings from configuration
     *
     * @return the settings from configuration {@link Settings}
     */
    Settings getSettings();
}
