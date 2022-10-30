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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The interface represents the settings used in a configuration.
 *
 * @see Settings#of(Map[])
 */
public interface Settings {

    /**
     * Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa. If the map is modified while an iteration over
     * the set is in progress (except through the iterator's own remove operation), the results of the iteration are undefined.
     * The set supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove,
     * Set.remove, removeAll, retainAll, and clear operations. It does not support the add or addAll operations.
     *
     * @return a set view of the keys contained in this map
     */
    Set<String> keySet();

    /**
     * Converts the settings to {@link Map}
     *
     * @return a {@link Map} instance from {@link Settings}
     */
    Map<String, Object> toMap();

    /**
     * Returns the value to which the specified key is mapped, or {@link Optional#empty()} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@link Optional#empty()} if this map contains no mapping for the key
     * @throws NullPointerException when key is null
     */
    Optional<Object> get(String key);

    /**
     * Returns the value to which the specified from one of these keys is mapped, or {@link Optional#empty()}
     * if this map contains no mapping for the key.
     *
     * @param keys the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@link Optional#empty()}
     * if this map contains no mapping for the key
     * @throws NullPointerException when keys is null
     */
    Optional<Object> get(Collection<String> keys);

    /**
     * Finds all keys that have the parameter as a prefix
     *
     * @param prefix the prefix
     * @return all the keys from prefix
     * @throws NullPointerException when prefix is null
     */
    List<Object> prefix(String prefix);

    /**
     * Finds all keys that have the parameter as a prefix
     *
     * @param prefixes the list of prefixes
     * @return all the keys from prefix
     * @throws NullPointerException when prefixes is null
     */
    List<Object> prefix(Collection<String> prefixes);

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key  the key whose associated value is to be returned
     * @param type the type be used as {@link Value#get(Class)}
     * @param <T>  the type value
     * @return the value to which the specified key is mapped, or {@link Optional#empty()} if this map contains no mapping for the key
     * @throws NullPointerException when there are null parameters
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key
     */
    Object getOrDefault(String key, Object defaultValue);

    /**
     * @return Returns true if this map contains no key-value mappings.
     */
    boolean isEmpty();

    /**
     * @return Returns the number of key-value mappings in this map.
     */
    int size();

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     * @throws NullPointerException when key is null
     */
    boolean containsKey(String key);

    /**
     * Returns a Set view of the mappings contained in this map.
     *
     * @return a set view of the mappings contained in this map
     */
    Set<Map.Entry<String, Object>> entrySet();

    /**
     * Performs the given action for each entry in this map until all entries have been processed or the action throws an exception.
     *
     * @param action the action
     * @throws NullPointerException when action is null
     */
    void forEach(BiConsumer<String, Object> action);

    /**
     * If the value for the specified key is present and non-null, attempts to compute a new mapping given the key and its current mapped value.
     *
     * @param key    the key
     * @param action the action
     * @throws NullPointerException when there is null parameter
     */
    void computeIfPresent(String key, BiConsumer<String, Object> action);

    /**
     * If the specified key is not already associated with a value (or is mapped to null),
     * attempts to compute its value using the given mapping function and enters it into this map unless null.
     *
     * @param key    the key
     * @param action the action
     * @throws NullPointerException when there is null parameter
     */
    void computeIfAbsent(String key, Function<String, Object> action);

    /**
     * Creates a {@link SettingsBuilder}
     *
     * @return a {@link SettingsBuilder} instance
     */
    static SettingsBuilder builder() {
        return ServiceLoaderProvider.get(SettingsBuilderProvider.class,
                () -> ServiceLoader.load(SettingsBuilderProvider.class)).get();
    }

    /**
     * Creates a settings from maps
     *
     * @param settings the setting
     * @return the new {@link Settings} instance
     * @throws NullPointerException when either the parameter is null or there key or value null
     */
    static Settings of(Map<String, Object> settings) {

        requireNonNull(settings, "settings is required");
        SettingsBuilder builder = builder();
        builder.putAll(settings);
        return builder.build();
    }

    /**
     * Creates a settings from maps
     *
     * @param settings the setting
     * @return the new {@link Settings} instance
     * @throws NullPointerException when either the parameter is null or there key or value null
     */
    @SafeVarargs
    static Settings of(Map<String, Object>... settings) {
        Stream.of(settings).forEach(s -> requireNonNull(s, "Settings is required"));

        SettingsBuilder builder = builder();
        Stream.of(settings).forEach(builder::putAll);
        return builder.build();
    }

    /**
     * The Settings builder {@link Settings}
     */
    interface SettingsBuilder {

        /**
         * Adds a new element in the builder
         *
         * @param key   the key to the settings
         * @param value the value from the respective settings
         * @return the settings builder with a new element
         * @throws NullPointerException when either key or value are null
         */
        SettingsBuilder put(String key, Object value);

        /**
         * Adds all elements in the builder
         *
         * @param settings the map with all elements
         * @return the settings builder with a new element
         * @throws NullPointerException when either the settings or the key or the value are null
         */
        SettingsBuilder putAll(Map<String, Object> settings);

        /**
         * Creates a {@link Settings} from the builder
         *
         * @return a {@link Settings} instance
         */
        Settings build();
    }

    /**
     * A provider of {@link SettingsBuilder}
     */
    interface SettingsBuilderProvider extends Supplier<SettingsBuilder> {

    }
}
