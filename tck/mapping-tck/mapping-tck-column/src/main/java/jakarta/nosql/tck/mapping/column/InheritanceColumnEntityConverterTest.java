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

package jakarta.nosql.tck.mapping.column;

import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.mapping.MappingException;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.tck.entities.inheritance.EmailNotification;
import jakarta.nosql.tck.entities.inheritance.LargeProject;
import jakarta.nosql.tck.entities.inheritance.Project;
import jakarta.nosql.tck.entities.inheritance.SmallProject;
import jakarta.nosql.tck.entities.inheritance.SmsNotification;
import jakarta.nosql.tck.entities.inheritance.SocialMediaNotification;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
public class InheritanceColumnEntityConverterTest {


    @Inject
    private ColumnEntityConverter converter;

    @Test
    public void shouldConvertProjectToSmallProject() {
        ColumnEntity entity = ColumnEntity.of("Project");
        entity.add("_id", "Small Project");
        entity.add("investor", "Otavio Santana");
        entity.add("size", "Small");
        Project project = this.converter.toEntity(entity);
        assertEquals("Small Project", project.getName());
        assertEquals(SmallProject.class, project.getClass());
        SmallProject smallProject = SmallProject.class.cast(project);
        assertEquals("Otavio Santana", smallProject.getInvestor());
    }

    @Test
    public void shouldConvertProjectToLargeProject() {
        ColumnEntity entity = ColumnEntity.of("Project");
        entity.add("_id", "Large Project");
        entity.add("budget", BigDecimal.TEN);
        entity.add("size", "Large");
        Project project = this.converter.toEntity(entity);
        assertEquals("Large Project", project.getName());
        assertEquals(LargeProject.class, project.getClass());
        LargeProject smallProject = LargeProject.class.cast(project);
        assertEquals(BigDecimal.TEN, smallProject.getBudget());
    }

    @Test
    public void shouldConvertLargeProjectToCommunicationEntity() {
        LargeProject project = new LargeProject();
        project.setName("Large Project");
        project.setBudget(BigDecimal.TEN);
        ColumnEntity entity = this.converter.toColumn(project);
        assertNotNull(entity);
        assertEquals("Project", entity.getName());
        assertEquals(project.getName(), entity.find("_id", String.class).get());
        assertEquals(project.getBudget(), entity.find("budget", BigDecimal.class).get());
        assertEquals("Large", entity.find("size", String.class).get());
    }

    @Test
    public void shouldConvertSmallProjectToCommunicationEntity() {
        SmallProject project = new SmallProject();
        project.setName("Small Project");
        project.setInvestor("Otavio Santana");
        ColumnEntity entity = this.converter.toColumn(project);
        assertNotNull(entity);
        assertEquals("Project", entity.getName());
        assertEquals(project.getName(), entity.find("_id", String.class).get());
        assertEquals(project.getInvestor(), entity.find("investor", String.class).get());
        assertEquals("Small", entity.find("size", String.class).get());
    }

    @Test
    public void shouldConvertColumnEntityToSocialMedia(){
        LocalDate date = LocalDate.now();
        ColumnEntity entity = ColumnEntity.of("Notification");
        entity.add("_id", 100L);
        entity.add("name", "Social Media");
        entity.add("nickname", "otaviojava");
        entity.add("createdOn",date);
        entity.add("type", SocialMediaNotification.class.getSimpleName());
        SocialMediaNotification notification = this.converter.toEntity(entity);
        assertEquals(100L, notification.getId());
        assertEquals("Social Media", notification.getName());
        assertEquals("otaviojava", notification.getNickname());
        assertEquals(date, notification.getCreatedOn());
    }

    @Test
    public void shouldConvertColumnEntityToSms(){
        LocalDate date = LocalDate.now();
        ColumnEntity entity = ColumnEntity.of("Notification");
        entity.add("_id", 100L);
        entity.add("name", "SMS Notification");
        entity.add("phone", "+351987654123");
        entity.add("createdOn", date);
        entity.add("type", "SMS");
        SmsNotification notification = this.converter.toEntity(entity);
        Assertions.assertEquals(100L, notification.getId());
        Assertions.assertEquals("SMS Notification", notification.getName());
        Assertions.assertEquals("+351987654123", notification.getPhone());
        assertEquals(date, notification.getCreatedOn());
    }

    @Test
    public void shouldConvertColumnEntityToEmail(){
        LocalDate date = LocalDate.now();
        ColumnEntity entity = ColumnEntity.of("Notification");
        entity.add("_id", 100L);
        entity.add("name", "Email Notification");
        entity.add("email", "otavio@otavio.test");
        entity.add("createdOn", date);
        entity.add("type", "Email");
        EmailNotification notification = this.converter.toEntity(entity);
        Assertions.assertEquals(100L, notification.getId());
        Assertions.assertEquals("Email Notification", notification.getName());
        Assertions.assertEquals("otavio@otavio.test", notification.getEmail());
        assertEquals(date, notification.getCreatedOn());
    }

    @Test
    public void shouldConvertSocialMediaToCommunicationEntity(){
        SocialMediaNotification notification = new SocialMediaNotification();
        notification.setId(100L);
        notification.setName("Social Media");
        notification.setCreatedOn(LocalDate.now());
        notification.setNickname("otaviojava");
        ColumnEntity entity = this.converter.toColumn(notification);
        assertNotNull(entity);
        assertEquals("Notification", entity.getName());
        assertEquals(notification.getId(), entity.find("_id", Long.class).get());
        assertEquals(notification.getName(), entity.find("name", String.class).get());
        assertEquals(notification.getNickname(), entity.find("nickname", String.class).get());
        assertEquals(notification.getCreatedOn(), entity.find("createdOn", LocalDate.class).get());
    }

    @Test
    public void shouldConvertSmsToCommunicationEntity(){
        SmsNotification notification = new SmsNotification();
        notification.setId(100L);
        notification.setName("SMS");
        notification.setCreatedOn(LocalDate.now());
        notification.setPhone("+351123456987");
        ColumnEntity entity = this.converter.toColumn(notification);
        assertNotNull(entity);
        assertEquals("Notification", entity.getName());
        assertEquals(notification.getId(), entity.find("_id", Long.class).get());
        assertEquals(notification.getName(), entity.find("name", String.class).get());
        assertEquals(notification.getPhone(), entity.find("phone", String.class).get());
        assertEquals(notification.getCreatedOn(), entity.find("createdOn", LocalDate.class).get());
    }

    @Test
    public void shouldConvertEmailToCommunicationEntity(){
        EmailNotification notification = new EmailNotification();
        notification.setId(100L);
        notification.setName("Email Media");
        notification.setCreatedOn(LocalDate.now());
        notification.setEmail("otavio@otavio.test.com");
        ColumnEntity entity = this.converter.toColumn(notification);
        assertNotNull(entity);
        assertEquals("Notification", entity.getName());
        assertEquals(notification.getId(), entity.find("_id", Long.class).get());
        assertEquals(notification.getName(), entity.find("name", String.class).get());
        assertEquals(notification.getEmail(), entity.find("email", String.class).get());
        assertEquals(notification.getCreatedOn(), entity.find("createdOn", LocalDate.class).get());
    }

    @Test
    public void shouldReturnErrorWhenConvertMissingColumn(){
        LocalDate date = LocalDate.now();
        ColumnEntity entity = ColumnEntity.of("Notification");
        entity.add("_id", 100L);
        entity.add("name", "SMS Notification");
        entity.add("phone", "+351987654123");
        entity.add("createdOn", date);
        Assertions.assertThrows(MappingException.class, ()-> this.converter.toEntity(entity));
    }

    @Test
    public void shouldReturnErrorWhenMismatchField() {
        LocalDate date = LocalDate.now();
        ColumnEntity entity = ColumnEntity.of("Notification");
        entity.add("_id", 100L);
        entity.add("name", "Email Notification");
        entity.add("email", "otavio@otavio.test");
        entity.add("createdOn", date);
        entity.add("type", "Wrong");
        Assertions.assertThrows(MappingException.class, ()-> this.converter.toEntity(entity));
    }
}
