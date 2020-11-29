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
package jakarta.nosql.tck.communication.driver.keyvalue;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import java.util.Objects;


public class User implements Serializable {

    private final String nickName;

    @JsonbCreator
    public User(@JsonbProperty("nickName") String nickName) {
        this.nickName = nickName;

    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickName, user.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("nickName='").append(nickName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
