/*
 * Copyright (c) 2022 Otavio Santana and others
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
module jakarta.nosql.mapping.tck.column {
    requires jakarta.nosql.mapping.tck.test;
    requires jakarta.nosql.mapping.column;
    requires javax.inject;
    requires jakarta.nosql.mapping.core;
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires jakarta.nosql.mapping.tck.entities;
    requires jakarta.nosql.communication.column;
    requires org.assertj.core;
    requires jakarta.nosql.communication.core;
    requires static org.apiguardian.api;
    exports jakarta.nosql.tck.mapping.column;
}