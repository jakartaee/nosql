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

import jakarta.annotation.Priority;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;

class ServiceLoaderSortTest {


    @Test
    public void shouldCreateWithPriority() {
        Person person = new Person();
        ServiceLoaderSort<Person> sort = ServiceLoaderSort.of(person);
        Assertions.assertEquals(10, sort.getPriority());
        Assertions.assertTrue(sort.get() instanceof Person);
    }

    @Test
    public void shouldCreateWithDefaultValues() {
        Machine machine = new Machine();
        ServiceLoaderSort<Machine> sort = ServiceLoaderSort.of(machine);
        Assertions.assertEquals(0, sort.getPriority());
        Assertions.assertTrue(sort.get() instanceof Machine);
    }

    @Test
    public void shouldReturnFirstHighPriority() {
        List<Serializable> things = of(new Computer(), new Animal(), new Machine(), new Person())
                .map(ServiceLoaderSort::of)
                .sorted()
                .map(s -> s.get())
                .collect(Collectors.toList());

        Assertions.assertTrue(things.get(0) instanceof Animal);
        Assertions.assertTrue(things.get(1) instanceof Person);
        Assertions.assertTrue(things.get(2) instanceof Computer);
        Assertions.assertTrue(things.get(3) instanceof Machine);
    }


    @Priority(10)
    static class Person implements Serializable {

    }

    @Priority(11)
    static class Animal implements Serializable {

    }

    static class Machine implements Serializable {

    }

    static class Computer implements Serializable {

    }
}