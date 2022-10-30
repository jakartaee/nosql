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
module jakarta.nosql.communication.document {
    requires jakarta.nosql.communication.core;
    requires jakarta.nosql.communication.query;

    exports jakarta.nosql.document;
    opens jakarta.nosql.document;

    uses jakarta.nosql.document.Document.DocumentProvider;
    uses jakarta.nosql.document.DocumentEntity.DocumentEntityProvider;
    uses jakarta.nosql.document.DocumentQueryParser;
    uses jakarta.nosql.document.DocumentCondition.DocumentConditionProvider;
    uses jakarta.nosql.document.DocumentConfiguration;
    uses jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteProvider;
    uses jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteQueryBuilderProvider;
    uses jakarta.nosql.document.DocumentQuery.DocumentSelectProvider;
    uses jakarta.nosql.document.DocumentQuery.DocumentQueryBuilderProvider;


}