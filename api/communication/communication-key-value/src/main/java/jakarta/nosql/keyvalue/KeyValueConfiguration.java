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
import java.util.function.Function;

/**
 * It is a function that reads from {@link Settings} and then creates a manager factory instance.
 * It should return a {@link NullPointerException} when the {@link Settings} parameter is null.
 *
 * @see BucketManagerFactory
 * @see BucketManager
 */
public interface KeyValueConfiguration extends Function<Settings, BucketManagerFactory> {

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
