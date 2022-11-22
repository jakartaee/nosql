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

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * A group of params to a dynamic query
 */
public interface Params {

    /**
     * @return if the params list is not empty
     */
    boolean isNotEmpty();

    /**
     * Adds a new value at the params
     *
     * @param param the param
     * @return the {@link Value}
     */
    Value add(String param);

    /**
     * @return the parameters name at the params
     */
    List<String> getParametersNames();

    /**
     * set the value from the class
     *
     * @param name  the name
     * @param value the value
     */
    void bind(String name, Object value);

    /**
     * It returns a new Params instance
     *
     * @return a new {@link Params} instance
     */
    static Params newParams() {
        return ServiceLoaderProvider.get(ParamsProvider.class
        , () -> ServiceLoader.load(ParamsProvider.class)).get();
    }

    /**
     * A {@link Params} supplier
     */
    interface ParamsProvider extends Supplier<Params> {

    }
}
