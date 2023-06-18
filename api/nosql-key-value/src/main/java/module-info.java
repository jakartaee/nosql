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
/**
 * The Jakarta NoSQL key-value module.
 * <p>
 * Key-value (KV) stores use the associative array (also known as a map or dictionary) as their fundamental data model.
 * In this model, data is represented as a collection of key-value pairs, such that each possible key appears at most
 * once in the collection.
 * The key-value model is one of the simplest non-trivial data models, and richer data models are often implemented
 * as an extension of it.
 * </p>
 */
module jakarta.nosql.keyvalue {
    requires jakarta.nosql.core;
    exports jakarta.nosql.keyvalue;
}