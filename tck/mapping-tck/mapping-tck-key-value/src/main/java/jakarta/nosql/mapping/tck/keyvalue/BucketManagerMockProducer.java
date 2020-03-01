/*
 * Copyright (c) 2020 Otavio Santana and others
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
package jakarta.nosql.mapping.tck.keyvalue;


import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.User;
import org.mockito.Mockito;

import javax.enterprise.inject.Produces;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class BucketManagerMockProducer {

    @Produces
    public BucketManager getBucketManager() {
        BucketManager bucketManager = Mockito.mock(BucketManager.class);
        Person person = Person.builder().withName("Default").build();
        when(bucketManager.get("key")).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get(10L)).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get("user")).thenReturn(Optional.of(Value.of(new User("Default", "Default", 10))));
        return bucketManager;
    }

    @Produces
    @Database(value = DatabaseType.KEY_VALUE, provider = "keyvalueMock")
    public BucketManager getBucketManagerMock() {
        BucketManager bucketManager = Mockito.mock(BucketManager.class);
        Person person = Person.builder().withName("keyvalueMock").build();
        when(bucketManager.get("key")).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get(10L)).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get("user")).thenReturn(Optional.of(Value.of(new User("keyvalueMock", "keyvalueMock", 10))));
        return bucketManager;
    }


}
