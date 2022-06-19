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
module jakarata.nosql.communication.column {
    requires jakarta.nosql.communication.core;
    requires jakarta.nosql.communication.query;

    exports jakarta.nosql.column;
    opens jakarta.nosql.column;

    uses jakarta.nosql.column.Column.ColumnProvider;
    uses jakarta.nosql.column.ColumnEntity.ColumnEntityProvider;
    uses jakarta.nosql.column.ColumnQueryParser;
    uses jakarta.nosql.column.ColumnCondition.ColumnConditionProvider;
    uses jakarta.nosql.column.ColumnConfiguration;
    uses jakarta.nosql.column.ColumnDeleteQuery.ColumnDeleteProvider;
    uses jakarta.nosql.column.ColumnDeleteQuery.ColumnDeleteQueryBuilderProvider;
    uses jakarta.nosql.column.ColumnQuery.ColumnSelectProvider;
    uses jakarta.nosql.column.ColumnQuery.ColumnQueryBuilderProvider;

}