/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import jakarta.nosql.TypeReference;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.mapping.MappingException;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.tck.entities.inheritance.EmailNotification;
import jakarta.nosql.tck.entities.inheritance.LargeProject;
import jakarta.nosql.tck.entities.inheritance.Notification;
import jakarta.nosql.tck.entities.inheritance.NotificationReader;
import jakarta.nosql.tck.entities.inheritance.Project;
import jakarta.nosql.tck.entities.inheritance.ProjectManager;
import jakarta.nosql.tck.entities.inheritance.SmallProject;
import jakarta.nosql.tck.entities.inheritance.SmsNotification;
import jakarta.nosql.tck.entities.inheritance.SocialMediaNotification;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
public class ColumnEntityConverterInheritanceTest {


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
    public void shouldConvertProject() {
        ColumnEntity entity = ColumnEntity.of("Project");
        entity.add("_id", "Project");
        entity.add("size", "Project");
        Project project = this.converter.toEntity(entity);
        assertEquals("Project", project.getName());
    }

    @Test
    public void shouldConvertProjectToCommunicationEntity() {
        Project project = new Project();
        project.setName("Large Project");
        ColumnEntity entity = this.converter.toColumn(project);
        assertNotNull(entity);
        assertEquals("Project", entity.getName());
        assertEquals(project.getName(), entity.find("_id", String.class).get());
        assertEquals("Project", entity.find("size", String.class).get());
    }

    @Test
    public void shouldConvertColumnEntityToSocialMedia(){
        LocalDate date = LocalDate.now();
        ColumnEntity entity = ColumnEntity.of("Notification");
        entity.add("_id", 100L);
        entity.add("name", "Social Media");
        entity.add("nickname", "otaviojava");
        entity.add("createdOn",date);
        entity.add("dtype", SocialMediaNotification.class.getSimpleName());
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
        entity.add("dtype", "SMS");
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
        entity.add("dtype", "Email");
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
        entity.add("dtype", "Wrong");
        Assertions.assertThrows(MappingException.class, ()-> this.converter.toEntity(entity));
    }


    @Test
    public void shouldConvertCommunicationNotificationReaderEmail() {
        ColumnEntity entity = ColumnEntity.of("NotificationReader");
        entity.add("_id", "poli");
        entity.add("name", "Poliana Santana");
        entity.add("notification", Arrays.asList(
                Column.of("_id", 10L),
                Column.of("name", "News"),
                Column.of("email", "otavio@email.com"),
                Column.of("_id", LocalDate.now()),
                Column.of("dtype", "Email")
        ));

        NotificationReader notificationReader = converter.toEntity(entity);
        assertNotNull(notificationReader);
        Assertions.assertEquals("poli", notificationReader.getNickname());
        Assertions.assertEquals("Poliana Santana", notificationReader.getName());
        Notification notification = notificationReader.getNotification();
        assertNotNull(notification);
        Assertions.assertEquals(EmailNotification.class, notification.getClass());
        EmailNotification email = (EmailNotification) notification;
        Assertions.assertEquals(10L, email.getId());
        Assertions.assertEquals("News", email.getName());
        Assertions.assertEquals("otavio@email.com", email.getEmail());
    }

    @Test
    public void shouldConvertCommunicationNotificationReaderSms() {
        ColumnEntity entity = ColumnEntity.of("NotificationReader");
        entity.add("_id", "poli");
        entity.add("name", "Poliana Santana");
        entity.add("notification", Arrays.asList(
                Column.of("_id", 10L),
                Column.of("name", "News"),
                Column.of("phone", "123456789"),
                Column.of("_id", LocalDate.now()),
                Column.of("dtype", "SMS")
        ));

        NotificationReader notificationReader = converter.toEntity(entity);
        assertNotNull(notificationReader);
        Assertions.assertEquals("poli", notificationReader.getNickname());
        Assertions.assertEquals("Poliana Santana", notificationReader.getName());
        Notification notification = notificationReader.getNotification();
        assertNotNull(notification);
        Assertions.assertEquals(SmsNotification.class, notification.getClass());
        SmsNotification sms = (SmsNotification) notification;
        Assertions.assertEquals(10L, sms.getId());
        Assertions.assertEquals("News", sms.getName());
        Assertions.assertEquals("123456789", sms.getPhone());
    }

    @Test
    public void shouldConvertCommunicationNotificationReaderSocial() {
        ColumnEntity entity = ColumnEntity.of("NotificationReader");
        entity.add("_id", "poli");
        entity.add("name", "Poliana Santana");
        entity.add("notification", Arrays.asList(
                Column.of("_id", 10L),
                Column.of("name", "News"),
                Column.of("nickname", "123456789"),
                Column.of("_id", LocalDate.now()),
                Column.of("dtype", "SocialMediaNotification")
        ));

        NotificationReader notificationReader = converter.toEntity(entity);
        assertNotNull(notificationReader);
        Assertions.assertEquals("poli", notificationReader.getNickname());
        Assertions.assertEquals("Poliana Santana", notificationReader.getName());
        Notification notification = notificationReader.getNotification();
        assertNotNull(notification);
        Assertions.assertEquals(SocialMediaNotification.class, notification.getClass());
        SocialMediaNotification social = (SocialMediaNotification) notification;
        Assertions.assertEquals(10L, social.getId());
        Assertions.assertEquals("News", social.getName());
        Assertions.assertEquals("123456789", social.getNickname());
    }

    @Test
    public void shouldConvertSocialCommunication() {
        SocialMediaNotification notification = new SocialMediaNotification();
        notification.setId(10L);
        notification.setName("Ada");
        notification.setNickname("ada.lovelace");
        NotificationReader reader = new NotificationReader("otavio", "Otavio", notification);

        ColumnEntity entity = this.converter.toColumn(reader);
        assertNotNull(entity);

        assertEquals("NotificationReader", entity.getName());
        assertEquals("otavio", entity.find("_id", String.class).get());
        assertEquals("Otavio", entity.find("name", String.class).get());
        List<Column> columns = entity.find("notification", new TypeReference<List<Column>>() {
        }).get();

        assertThat(columns).contains(Column.of("_id", 10L),
                        Column.of("name", "Ada"),
                        Column.of("dtype", "SocialMediaNotification"),
                        Column.of("nickname", "ada.lovelace"));
    }

    @Test
    public void shouldConvertConvertProjectManagerCommunication() {
        LargeProject large = new LargeProject();
        large.setBudget(BigDecimal.TEN);
        large.setName("large");

        SmallProject small = new SmallProject();
        small.setInvestor("new investor");
        small.setName("Start up");

        List<Project> projects = new ArrayList<>();
        projects.add(large);
        projects.add(small);

        ProjectManager manager = ProjectManager.of(10L, "manager", projects);
        ColumnEntity entity = this.converter.toColumn(manager);
        assertNotNull(entity);

        assertEquals("ProjectManager", entity.getName());
        assertEquals(10L, entity.find("_id", Long.class).get());
        assertEquals("manager", entity.find("name", String.class).get());

        List<List<Column>> columns = (List<List<Column>>) entity.find("projects").get().get();

        List<Column> largeCommunication = columns.get(0);
        List<Column> smallCommunication = columns.get(1);
        assertThat(largeCommunication).contains(
                Column.of("_id", "large"),
                Column.of("size", "Large"),
                Column.of("budget", BigDecimal.TEN)
        );

        assertThat(smallCommunication).contains(
                Column.of("size", "Small"),
                Column.of("investor", "new investor"),
                Column.of("_id", "Start up")
        );

    }

    @Test
    public void shouldConvertConvertCommunicationProjectManager() {
        ColumnEntity communication = ColumnEntity.of("ProjectManager");
        communication.add("_id", 10L);
        communication.add("name", "manager");
        List<List<Column>> columns = new ArrayList<>();
        columns.add(Arrays.asList(
                Column.of("_id","small-project"),
                Column.of("size","Small"),
                Column.of("investor","investor")
        ));
        columns.add(Arrays.asList(
                Column.of("_id","large-project"),
                Column.of("size","Large"),
                Column.of("budget",BigDecimal.TEN)
        ));
        communication.add("projects", columns);

        ProjectManager manager = converter.toEntity(communication);
        assertNotNull(manager);

        assertEquals(10L, manager.getId());
        assertEquals("manager", manager.getName());

        List<Project> projects = manager.getProjects();
        assertEquals(2, projects.size());
        SmallProject small = (SmallProject) projects.get(0);
        LargeProject large = (LargeProject) projects.get(1);
        assertNotNull(small);
        assertEquals("small-project", small.getName());
        assertEquals("investor", small.getInvestor());

        assertNotNull(large);
        assertEquals("large-project", large.getName());
        assertEquals(BigDecimal.TEN, large.getBudget());

    }

}
