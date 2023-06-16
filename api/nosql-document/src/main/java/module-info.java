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
 * The Jakarta NoSQL document module.
 * <p>
 * A document-oriented database, or document store, is a computer program designed for storing, retrieving, and managing document-oriented information,
 * also known as semi-structured data. Document-oriented databases are one of the main categories of NoSQL databases,
 * and the popularity of the term "document-oriented database" has grown with the use of the term NoSQL itself.
 * </p>
 */
module jakarta.nosql.document {
    requires jakarta.nosql.core;
    exports jakarta.nosql.document;
}