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
package jakarta.nosql.mapping.tck.reflection;

import jakarta.nosql.mapping.reflection.Reflections;
import jakarta.nosql.tck.entities.Actor;
import jakarta.nosql.tck.entities.Movie;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;


@CDIExtension(classes = {Actor.class, Movie.class, Person.class})
public class ReflectionsTest {


    @Inject
    private Reflections reflections;

    @Test
    public void shouldReturnsEntityName() {
        assertEquals("Person", reflections.getEntityName(Person.class));
        assertEquals("movie", reflections.getEntityName(Movie.class));
    }

    @Test
    public void shouldListFields() {

        assertEquals(4, reflections.getFields(Person.class).size());
        assertEquals(6, reflections.getFields(Actor.class).size());

    }

    @Test
    public void shouldReturnColumnName() throws NoSuchFieldException {
        Field phones = Person.class.getDeclaredField("phones");
        Field id = Person.class.getDeclaredField("id");

        assertEquals("phones", reflections.getColumnName(phones));
        assertEquals("id", reflections.getColumnName(id));
        assertEquals("_id", reflections.getIdName(id));
    }

}