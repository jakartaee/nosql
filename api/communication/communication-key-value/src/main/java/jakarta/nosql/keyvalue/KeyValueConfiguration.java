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

package jakarta.nosql.keyvalue;

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Settings;

import java.util.ServiceLoader;

/**
 * The Jakarta NoSQL configuration to create a {@link BucketManagerFactory}
 */
public interface KeyValueConfiguration {

    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider
     * and then creates a {@link BucketManagerFactory} instance.
     *
     * @param <T> the BucketManagerFactory type
     * @return a {@link BucketManagerFactory} instance
     */
    <T extends BucketManagerFactory> T get();

    /**
     * Reads configuration from the {@link Settings} instance, the parameters are defined by NoSQL
     * provider, then creates a {@link BucketManagerFactory} instance.
     *
     * @param <T>      the BucketManagerFactory type
     * @param settings the settings
     * @return a {@link BucketManagerFactory}
     * @throws NullPointerException when settings is null
     * @see Settings
     * @see Settings {@link java.util.Map}
     */
    <T extends BucketManagerFactory> T get(Settings settings);

    /**
     * creates and returns a  {@link KeyValueConfiguration}  instance from {@link java.util.ServiceLoader}
     *
     * @param <T> the configuration type
     * @return {@link KeyValueConfiguration} instance
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     * @throws jakarta.nosql.NonUniqueResultException  when there is more than one KeyValueConfiguration
     */
    static <T extends KeyValueConfiguration> T getConfiguration() {
        return (T) ServiceLoaderProvider.getUnique(KeyValueConfiguration.class,
                () -> ServiceLoader.load(KeyValueConfiguration.class));
    }

    /**
     * creates and returns a  {@link KeyValueConfiguration} instance from {@link java.util.ServiceLoader}
     * for a particular provider implementation.
     *
     * @param <T>      the configuration type
     * @param service the particular provider
     * @return {@link KeyValueConfiguration} instance
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     * @throws jakarta.nosql.NonUniqueResultException  when there is more than one KeyValueConfiguration
     */
    static <T extends KeyValueConfiguration> T getConfiguration(Class<T> service) {
        return ServiceLoaderProvider.getUnique(KeyValueConfiguration.class, () ->
                ServiceLoader.load(KeyValueConfiguration.class), service);
    }
}
