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
package jakarta.nosql.mapping.configuration;

import jakarta.nosql.mapping.ConfigurationUnit;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

/**
 * The reader of configurations
 */
public interface ConfigurableReader {

    /**
     * That reads the configurations and than returns {@link Configurable}
     * @param stream the stream
     * @param annotation the annotation
     * @return the configurations list
     * @throws NullPointerException when either stream or annotation are null
     * @throws ConfigurationException when has configuration problem
     */
    List<Configurable> read(Supplier<InputStream> stream, ConfigurationUnit annotation);
}
