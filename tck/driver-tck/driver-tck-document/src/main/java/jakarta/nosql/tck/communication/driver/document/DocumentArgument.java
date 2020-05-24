/*
 *  Copyright (c) 2020 Otavio Santana and others
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v. 2.0 which is available at
 *  http://www.eclipse.org/legal/epl-2.0.
 *
 *  This Source Code may also be made available under the following Secondary
 *  Licenses when the conditions for such availability set forth in the Eclipse
 *  Public License v. 2.0 are satisfied: GNU General Public License v2.0
 *  w/Classpath exception which is available at
 *  https://www.gnu.org/software/classpath/license.html.
 *
 *  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.communication.driver.document;

import java.util.Collections;
import java.util.List;

public final class DocumentArgument {

    static final DocumentArgument EMPTY = new DocumentArgument();

    private final List<String> query;

    private final String idName;

    private final boolean empty;

    DocumentArgument(List<String> query, String idName) {
        this.query = query;
        this.idName = idName;
        this.empty = true;
    }

    DocumentArgument() {
        this.query = null;
        this.idName = null;
        this.empty = false;
    }


    public List<String> getQuery() {
        if (query == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(query);
    }

    public String getIdName() {
        return idName;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "DocumentArgument{" +
                "query=" + query +
                ", idName='" + idName + '\'' +
                ", empty=" + empty +
                '}';
    }
}
