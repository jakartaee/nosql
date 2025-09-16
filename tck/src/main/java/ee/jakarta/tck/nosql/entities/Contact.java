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
public class Contact {

    @Id
    private String name;

    @Column
    private Map<String, String> socialMedia;


    public String getName() {
        return name;
    }

    public Map<String, String> getSocialMedia() {
        return Collections.unmodifiableMap(socialMedia);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", socialMedia=" + socialMedia +
                '}';
    }

    public static Contact of(String name, Map<String, String> socialMedia) {
        Contact contact = new Contact();
        contact.name = name;
        contact.socialMedia = socialMedia;
        return contact;
    }

    public void put(String socialMedia, String url) {
        this.socialMedia.put(socialMedia, url);
    }
}
