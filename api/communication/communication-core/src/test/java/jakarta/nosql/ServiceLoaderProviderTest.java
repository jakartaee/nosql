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
package jakarta.nosql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceLoaderProviderTest {


    @Test
    public void shouldReturnExceptionWhenThereNotImplementation() {
        assertThrows(ProviderNotFoundException.class, () -> ServiceLoaderProvider.get(Unicorn.class));
    }

    @Test
    public void shouldReturnImplementation() {
        Animal animal = ServiceLoaderProvider.get(Animal.class);
        Assertions.assertNotNull(animal);
        Assertions.assertTrue(animal instanceof Horse);
    }

    @Test
    public void shouldReturnWithPriority() {
        Machine machine = ServiceLoaderProvider.get(Machine.class);
        Assertions.assertNotNull(machine);
        Assertions.assertTrue(machine instanceof Computer);
    }


    @Test
    public void shouldErrorWithMoreThanOneImplementation() {
        Assertions.assertThrows(NonUniqueResultException.class, () ->
                ServiceLoaderProvider.getUnique(Machine.class));
    }

    @Test
    public void shouldReturnUnique() {
        Animal animal = ServiceLoaderProvider.getUnique(Animal.class);
        Assertions.assertNotNull(animal);
        Assertions.assertTrue(animal instanceof Horse);
    }

    @Test
    public void shouldReturnOneWithPredicate() {
        final Predicate<Object> computer = o -> o.getClass().getSimpleName().equals("Computer");
        Machine machine = ServiceLoaderProvider.getUnique(Machine.class, computer);
        Assertions.assertNotNull(machine);
        Assertions.assertTrue(machine instanceof Computer);
    }

    @Test
    public void shouldReturnOneWithClass() {
        Mobile machine = ServiceLoaderProvider.getUnique(Machine.class, Mobile.class);
        Assertions.assertNotNull(machine);
        Assertions.assertTrue(machine instanceof Mobile);
    }
}