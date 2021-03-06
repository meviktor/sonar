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
package org.sonar.core.persistence.dialect;

import org.hibernate.id.PersistentIdentifierGenerator;
import org.junit.Test;

import java.util.Properties;

import static org.fest.assertions.Assertions.assertThat;

public class OracleSequenceGeneratorTest {

  @Test
  public void sequenceNameShouldFollowRailsConventions() {
    Properties props = new Properties();
    props.setProperty(PersistentIdentifierGenerator.TABLE, "my_table");
    props.setProperty(PersistentIdentifierGenerator.PK, "id");

    OracleSequenceGenerator generator = new OracleSequenceGenerator();
    generator.configure(null, props, new Oracle.Oracle10gWithDecimalDialect());
    assertThat(generator.getSequenceName()).isEqualTo("MY_TABLE_SEQ");
  }

  @Test
  public void should_not_fail_if_table_name_can_not_be_loaded() {
    Properties props = new Properties();
    OracleSequenceGenerator generator = new OracleSequenceGenerator();
    generator.configure(null, props, new Oracle.Oracle10gWithDecimalDialect());
    assertThat(generator.getSequenceName()).isNotEmpty();
  }
}
