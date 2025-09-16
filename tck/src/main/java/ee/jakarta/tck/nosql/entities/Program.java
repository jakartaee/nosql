/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package ee.jakarta.tck.nosql.entities;


import jakarta.nosql.Column;
import jakarta.nosql.Entity;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Entity
public class Program {


    @Column
    private String name;

    @Column
    private Map<String, String> info;


    public String getName() {
        return name;
    }

    public Map<String, String> getInfo() {
        return Collections.unmodifiableMap(info);
    }

    @Override
    public String toString() {
        return "Program{" +
                "name='" + name + '\'' +
                ", info=" + info +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Program program = (Program) o;
        return Objects.equals(name, program.name) && Objects.equals(info, program.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, info);
    }

    public static Program of(String name, Map<String, String> socialMedia) {
        Program program = new Program();
        program.name = name;
        program.info = socialMedia;
        return program;
    }
}
