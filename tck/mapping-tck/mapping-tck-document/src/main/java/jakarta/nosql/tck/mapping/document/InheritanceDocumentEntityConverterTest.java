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
import jakarta.nosql.tck.entities.inheritance.LargeProject;
import jakarta.nosql.tck.entities.inheritance.Project;
import jakarta.nosql.tck.entities.inheritance.SmallProject;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

@CDIExtension
public class InheritanceDocumentEntityConverterTest {

    @Inject
    private DocumentEntityConverter converter;

    @Test
    public void shouldConvertProjectToSmallProject() {
        DocumentEntity entity = DocumentEntity.of("Project");
        entity.add("name", "Small Project");
        entity.add("investor", "Otavio Santana");
        entity.add("size", "Small");
        Project project = this.converter.toEntity(entity);
        Assertions.assertEquals("Small Project", project.getName());
        Assertions.assertEquals(SmallProject.class, project.getClass());
        SmallProject smallProject = SmallProject.class.cast(project);
        Assertions.assertEquals("Otavio Santana", smallProject.getInvestor());
    }

    @Test
    public void shouldConvertProjectToLargeProject() {
        DocumentEntity entity = DocumentEntity.of("Project");
        entity.add("name", "Large Project");
        entity.add("budget", BigDecimal.TEN);
        entity.add("size", "Large");
        Project project = this.converter.toEntity(entity);
        Assertions.assertEquals("Large Project", project.getName());
        Assertions.assertEquals(LargeProject.class, project.getClass());
        LargeProject smallProject = LargeProject.class.cast(project);
        Assertions.assertEquals(BigDecimal.TEN, smallProject.getBudget());
    }

    @Test
    public void shouldConvertLargeProjectToDocumentEntity() {
        LargeProject project = new LargeProject();
        project.setName("Large Project");
        project.setBudget(BigDecimal.TEN);
        DocumentEntity entity = this.converter.toDocument(project);
        Assertions.assertNotNull(entity);
        Assertions.assertEquals("Project", entity.getName());
        Assertions.assertEquals(project.getName(), entity.find("name", String.class).get());
        Assertions.assertEquals(project.getBudget(), entity.find("budget", BigDecimal.class).get());
        Assertions.assertEquals("Large", entity.find("type", String.class).get());
    }

    @Test
    public void shouldConvertSmallProjectToDocumentEntity() {
        SmallProject project = new SmallProject();
        project.setName("Small Project");
        project.setInvestor("Otavio Santana");
        DocumentEntity entity = this.converter.toDocument(project);
        Assertions.assertNotNull(entity);
        Assertions.assertEquals("Project", entity.getName());
        Assertions.assertEquals(project.getName(), entity.find("name", String.class).get());
        Assertions.assertEquals(project.getInvestor(), entity.find("investor", String.class).get());
        Assertions.assertEquals("Small", entity.find("type", String.class).get());
    }

}
