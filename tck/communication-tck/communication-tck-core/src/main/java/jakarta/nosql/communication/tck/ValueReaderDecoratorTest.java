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

package jakarta.nosql.communication.tck;

import jakarta.nosql.ValueReaderDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ValueReaderDecoratorTest {

    private final ValueReaderDecorator serviceLoader = ValueReaderDecorator.getInstance();

    @Test
    public void shouldConvert() {
        Number convert = serviceLoader.read(Number.class, "10D");
        assertEquals(10D, convert);
    }

    @Test
    public void shouldCastObject() {
        Bean name = serviceLoader.read(Bean.class, new Bean());
        assertEquals(name.getClass(), Bean.class);
    }

    @Test
    public void shouldReturnErrorWhenTypeIsNotSupported() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> serviceLoader.read(Bean.class, "name"));
    }

    @Test
    public void shouldReturnIfIsCompatible() {
        assertTrue(serviceLoader.isCompatible(Integer.class));
    }

    @Test
    public void shouldReturnIfIsNotCompatible() {
        assertFalse(serviceLoader.isCompatible(Bean.class));
    }


    static class Bean {
        private String name;

        Bean() {
            this.name = "name";
        }
    }

}
