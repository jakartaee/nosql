
/*
 * Copyright (c) 2020 Otavio Santana and others
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
module jakarta.nosql.communication.core {
    requires java.logging;
    requires java.annotation;
    requires static java.compiler;
    exports jakarta.nosql;

    uses jakarta.nosql.Params.ParamsProvider;
    uses jakarta.nosql.Settings.SettingsBuilderProvider;
    uses jakarta.nosql.Sort.SortProvider;
    uses jakarta.nosql.TypeSupplier;
    uses jakarta.nosql.Value.ValueProvider;
    uses jakarta.nosql.ValueReader;
    uses jakarta.nosql.ValueWriter;
    uses jakarta.nosql.TypeReferenceReader;

}