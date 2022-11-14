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
package jakarta.nosql.mapping.keyvalue;


import jakarta.nosql.keyvalue.BucketManager;

/**
 * The producer of {@link KeyValueTemplate}
 */
public interface KeyValueTemplateProducer {

    /**
     * creates a {@link KeyValueTemplate}
     *
     * @param <T>     the KeyValueTemplate instance
     * @param manager the manager
     * @return a new instance
     * @throws NullPointerException when manager is null
     */
    <T extends KeyValueTemplate> T get(BucketManager manager);
}
