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
import jakarta.nosql.Id;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Entity
public class Computer {

    @Id
    private String id;

    @Column
    private Map<String, Program> programs;

    public String getId() {
        return id;
    }

    public Map<String, Program> getPrograms() {
        return Collections.unmodifiableMap(programs);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Computer computer = (Computer) o;
        return Objects.equals(id, computer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id='" + id + '\'' +
                ", programs=" + programs +
                '}';
    }

    public static Computer of(String id, Map<String, Program> programs) {
        Computer computer = new Computer();
        computer.id = id;
        computer.programs = programs;
        return computer;
    }

    public void put(String programName, Program program) {
        this.programs.put(programName, program);
    }
}
