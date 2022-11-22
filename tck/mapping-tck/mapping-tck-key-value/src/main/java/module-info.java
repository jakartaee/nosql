/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
module jakarta.nosql.mapping.tck.key.value {
    requires jakarta.nosql.mapping.tck.test;
    requires jakarta.inject;
    requires jakarta.nosql.mapping.tck.entities;
    requires org.assertj.core;
    requires org.junit.jupiter.api;
    requires jakarta.nosql.mapping.core;
    requires jakarta.nosql.communication.key.value;
    requires jakarta.nosql.communication.core;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires static org.apiguardian.api;
    requires jakarta.nosql.mapping.key.value;
    exports jakarta.nosql.tck.mapping.keyvalue;
}