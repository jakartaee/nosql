/*
 * Copyright (c) 2022 Otavio Santana and others
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LoaderTypeTest {

    @Test
    public void shouldReturnServiceLoader() {
        LoaderType type = LoaderType.getLoaderType();
        Assertions.assertNotNull(type);
        Assertions.assertEquals(LoaderType.SERVICE_LOADER, type);
    }

    @Test
    public void shouldReadServiceLoader() {
        LoaderType type = LoaderType.SERVICE_LOADER;
        Stream<Object> stream = type.read(Machine.class);
        Assertions.assertNotNull(stream);
        List<Machine> machines = stream.map(Machine.class::cast).collect(Collectors.toList());
        Assertions.assertNotNull(machines);
        Assertions.assertEquals(2, machines.size());
    }
}