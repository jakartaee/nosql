/*
 * Copyright (c) 2022 Otavio Santana and others
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
package jakarta.nosql.tck.mapping.document;

import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.constructor.Computer;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@CDIExtension
public class DocumentEntityConverterConstructorTest {

    @Inject
    private DocumentEntityConverter converter;

    @Test
    public void shouldConverterEntityComputer() {
        DocumentEntity communication = DocumentEntity.of("Computer");
        communication.add("_id", 10L);
        communication.add("name", "Dell");
        communication.add("age", 2020);
        communication.add("model", "Dell 2020");
        communication.add("price", "USD 20");
        Computer computer = this.converter.toEntity(communication);
        Assertions.assertNotNull(computer);
        Assertions.assertEquals(10L, computer.getId());
        Assertions.assertEquals("Dell", computer.getName());
        Assertions.assertEquals(2020, computer.getAge());
        Assertions.assertEquals("Dell 2020", computer.getModel());
        Assertions.assertEquals(Money.parse("USD 20"), computer.getPrice());
    }

    @Test
    public void shouldConvertComputerToCommunication() {
        Computer computer = new Computer(10L, "Dell", 2020, "Dell 2020",
                Money.parse("USD 20"));
        DocumentEntity communication = this.converter.toDocument(computer);
        Assertions.assertNotNull(communication);

    }
}
