/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.core.batch;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import org.sonar.api.CoreProperties;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExcludedResourceFilterTest {

  @Test
  public void doNotFailIfNoPatterns() {
    PropertiesConfiguration conf = new PropertiesConfiguration();
    Project project = new Project("foo").setConfiguration(conf);
    ExcludedResourceFilter filter = new ExcludedResourceFilter(project);
    assertThat(filter.isIgnored(mock(Resource.class)), is(false));
  }

  @Test
  public void noPatternsMatch() {
    PropertiesConfiguration conf = new PropertiesConfiguration();
    conf.setProperty(CoreProperties.PROJECT_EXCLUSIONS_PROPERTY, new String[]{"**/foo/*.java", "**/bar/*"});
    Project project = new Project("foo").setConfiguration(conf);
    ExcludedResourceFilter filter = new ExcludedResourceFilter(project);
    assertThat(filter.isIgnored(mock(Resource.class)), is(false));
  }

  @Test
  public void ignoreResourceIfMatchesPattern() {
    PropertiesConfiguration conf = new PropertiesConfiguration();
    conf.setProperty(CoreProperties.PROJECT_EXCLUSIONS_PROPERTY, new String[]{"**/foo/*.java", "**/bar/*"});
    Project project = new Project("foo").setConfiguration(conf);
    ExcludedResourceFilter filter = new ExcludedResourceFilter(project);

    Resource resource = mock(Resource.class);
    when(resource.matchFilePattern("**/bar/*")).thenReturn(true);

    assertThat(filter.isIgnored(resource), is(true));
  }

  @Test
  public void ignoreTestIfMatchesPattern() {
    PropertiesConfiguration conf = new PropertiesConfiguration();
    conf.setProperty(CoreProperties.PROJECT_TEST_EXCLUSIONS_PROPERTY, new String[]{"**/foo/*.java", "**/bar/*"});
    Project project = new Project("foo").setConfiguration(conf);
    ExcludedResourceFilter filter = new ExcludedResourceFilter(project);

    Resource resource = mock(Resource.class);
    when(resource.getQualifier()).thenReturn(Qualifiers.UNIT_TEST_FILE);
    when(resource.matchFilePattern("**/bar/*")).thenReturn(true);

    assertThat(filter.isIgnored(resource), is(true));
  }

  /**
   * See SONAR-1115 Source exclusion patterns do not apply to unit tests.
   */
  @Test
  public void doNotExcludeUnitTestFiles() {
    PropertiesConfiguration conf = new PropertiesConfiguration();
    conf.setProperty(CoreProperties.PROJECT_EXCLUSIONS_PROPERTY, new String[]{"**/foo/*.java", "**/bar/*"});
    Project project = new Project("foo").setConfiguration(conf);
    ExcludedResourceFilter filter = new ExcludedResourceFilter(project);

    Resource unitTest = mock(Resource.class);
    when(unitTest.getQualifier()).thenReturn(Qualifiers.UNIT_TEST_FILE);

    // match exclusion pattern
    when(unitTest.matchFilePattern("**/bar/*")).thenReturn(true);

    assertThat(filter.isIgnored(unitTest), is(false));
  }
}
