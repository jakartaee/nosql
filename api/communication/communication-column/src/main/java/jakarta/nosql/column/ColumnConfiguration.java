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

package jakarta.nosql.column;

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Settings;

import java.util.ServiceLoader;


/**
 * The Jakarta NoSQL configuration to create a {@link ColumnManagerFactory}
 */
public interface ColumnConfiguration {

    /**
     * Reads configuration from the {@link Settings} instance, the parameters are defined by NoSQL
     * provider, then creates a {@link ColumnManagerFactory} instance.
     *
     * @param <T>      the ColumnFamilyManagerFactory type
     * @param settings the settings
     * @return a {@link ColumnManagerFactory}
     * @throws NullPointerException when settings is null
     * @see Settings
     * @see Settings {@link java.util.Map}
     */
    <T extends ColumnManagerFactory> T get(Settings settings);

    /**
     * creates and returns a  {@link ColumnConfiguration}  instance from {@link java.util.ServiceLoader}
     *
     * @param <T> the configuration type
     * @return {@link ColumnConfiguration} instance
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     * @throws jakarta.nosql.NonUniqueResultException  when there is more than one KeyValueConfiguration
     */
    static <T extends ColumnConfiguration> T getConfiguration() {
        return (T) ServiceLoaderProvider.getUnique(ColumnConfiguration.class,
                ()-> ServiceLoader.load(ColumnConfiguration.class));
    }

    /**
     * creates and returns a  {@link ColumnConfiguration} instance from {@link java.util.ServiceLoader}
     * for a particular provider implementation.
     *
     * @param <T>      the configuration type
     * @param service the particular provider
     * @return {@link ColumnConfiguration} instance
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     * @throws jakarta.nosql.NonUniqueResultException  when there is more than one KeyValueConfiguration
     */
    static <T extends ColumnConfiguration> T getConfiguration(Class<T> service) {
        return ServiceLoaderProvider.getUnique(ColumnConfiguration.class,
                ()-> ServiceLoader.load(ColumnConfiguration.class), service);
    }
}
