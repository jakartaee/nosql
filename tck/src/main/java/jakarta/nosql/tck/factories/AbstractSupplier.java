/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package jakarta.nosql.tck.factories;

import net.datafaker.Faker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * The {@code AbstractSupplier} class provides a base implementation for supplying entities instances
 * and also serves as an {@link ArgumentsProvider} for parameterized tests.
 *
 * <p>This abstract class leverages the {@link Faker} library to generate realistic data for entities.
 * The {@link Faker} instance is shared across all subclasses to ensure consistent and performant data generation.</p>
 *
 * <p>Subclasses should extend this class and implement the required methods to provide specific implementations
 * for supplying entities instances and providing arguments for tests.</p>
 *
 * <pre>
 * {@code
 * public class MyPersonSupplier extends AbstractSupplier<Person> {
 *     @Override
 *     public Person get() {
 *         return new Person(faker().name().fullName(), faker().address().streetAddress());
 *     }
 * }
 * }
 * </pre>
 *
 * @param <T> the type of object that this supplier provides
 *
 * @see Faker
 * @see Supplier
 * @see ArgumentsProvider
 * @since 1.0
 */
abstract class AbstractSupplier<T> implements Supplier<T>, ArgumentsProvider {

    private static final Faker FAKER = new Faker();

    /**
     * Provides access to the shared {@link Faker} instance for generating fake data.
     *
     * @return the shared {@link Faker} instance
     */
    protected Faker faker() {
        return FAKER;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(Arguments.of(get()));
    }
}
