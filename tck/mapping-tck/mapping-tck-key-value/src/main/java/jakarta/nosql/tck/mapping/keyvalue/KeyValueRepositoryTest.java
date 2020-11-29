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
package jakarta.nosql.tck.mapping.keyvalue;

import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.keyvalue.KeyValueRepositoryProducer;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.entities.User;
import jakarta.nosql.tck.test.CDIExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@CDIExtension
public class KeyValueRepositoryTest {

    @Mock
    private KeyValueTemplate template;

    @Inject
    private KeyValueRepositoryProducer producer;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
       this.userRepository = producer.get(UserRepository.class, template);
    }

    @Test
    public void shouldSave() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User user = new User("ada", "Ada", 10);
        userRepository.save(user);
        Mockito.verify(template).put(captor.capture());
        User value = captor.getValue();
        assertEquals(user, value);
    }


    @Test
    public void shouldSaveIterable() {
        ArgumentCaptor<Iterable> captor = ArgumentCaptor.forClass(Iterable.class);

        User user = new User("ada", "Ada", 10);
        userRepository.save(Collections.singleton(user));
        Mockito.verify(template).put(captor.capture());
        User value = (User) captor.getValue().iterator().next();
        assertEquals(user, value);
    }


    @Test
    public void shouldDelete() {
        userRepository.deleteById("key");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(template).delete(captor.capture());
        assertEquals("key", captor.getValue());
    }

    @Test
    public void shouldDeleteIterable() {
        userRepository.deleteById(Collections.singletonList("key"));
        ArgumentCaptor<Iterable> captor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(template).delete(captor.capture());
        assertEquals("key", captor.getValue().iterator().next());
    }

    @Test
    public void shouldFindById() {
        User user = new User("ada", "Ada", 10);
        when(template.get("key", User.class)).thenReturn(
                Optional.of(user));

        assertEquals(user, userRepository.findById("key").get());
    }

    @Test
    public void shouldFindByIdIterable() {
        User user = new User("ada", "Ada", 10);
        User user2 = new User("ada", "Ada", 10);
        List<String> keys = Arrays.asList("key", "key2");
        when(template.get(keys, User.class)).thenReturn(
                Arrays.asList(user, user2));

        assertThat(userRepository.findById(keys), Matchers.containsInAnyOrder(user, user2));
    }

    @Test
    public void shouldFindByQuery() {
        User user = new User("12", "Ada", 10);
        when(template.query("get \"12\"", User.class)).thenReturn(Stream.of(user));

        userRepository.findByQuery();
        verify(template).query("get \"12\"", User.class);

    }

    @Test
    public void shouldFindByQueryWithParameter() {
        User user = new User("12", "Ada", 10);
        List<String> keys = Arrays.asList("key", "key2");
        PreparedStatement prepare = Mockito.mock(PreparedStatement.class);
        when(template.prepare("get @id", User.class)).thenReturn(prepare);

        userRepository.findByQuery("id");
        verify(template).prepare("get @id", User.class);

    }

    @Test
    public void shouldReturnErrorWhenExecuteMethodQuery() {
        Assertions.assertThrows(DynamicQueryException.class, () -> userRepository.findByName("name"));
    }

    @Test
    public void shouldReturnToString() {
        assertNotNull(userRepository.toString());
    }

    @Test
    public void shouldReturnHasCode() {
        assertNotNull(userRepository.hashCode());
        assertEquals(userRepository.hashCode(), userRepository.hashCode());
    }

    @Test
    public void shouldReturnEquals() {
        assertNotNull(userRepository.equals(userRepository));
    }

    interface UserRepository extends Repository<User, String> {

        Optional<User> findByName(String name);

        @Query("get \"12\"")
        Optional<User> findByQuery();

        @Query("get @id")
        Optional<User> findByQuery(@Param("id") String id);
    }

}