/*******************************************************************************
 * Copyright (c) 2017 Martin Weber.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Martin Weber - initial implementation
 *******************************************************************************/
package de.marw.cmakeed.assist;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import com.cthing.cmakeed.core.commands.CMakeCommand;
import com.cthing.cmakeed.core.commands.CMakeCommands;
import com.cthing.cmakeed.core.properties.CMakeProperties;
import com.cthing.cmakeed.core.properties.CMakeProperty;
import com.cthing.cmakeed.core.reservedwords.CMakeReservedWord;
import com.cthing.cmakeed.core.reservedwords.CMakeReservedWords;
import com.cthing.cmakeed.core.variables.CMakeVariable;
import com.cthing.cmakeed.core.variables.CMakeVariables;

/**
 * Test that verify that the information from the com.cthing.cmakeed.core plugin
 * extension points are valid.
 *
 * @author weber
 *
 */
public class AssistDataTest {

  /**
   * Tests the command collection access method.
   */
  public void testGetCommands() {
    final Collection<CMakeCommand> cmds = CMakeCommands.getCommands();
    assertNotNull(cmds);
    assertTrue(cmds.size() > 1);
  }

  /**
   * Tests the command reserved word collection access method.
   */
  public void testGetReservedWords() {
    final Collection<CMakeReservedWord> cmds = CMakeReservedWords.getCommands();
    assertNotNull(cmds);
    assertTrue(cmds.size() > 1);
  }

  /**
   * Tests the property collection access method.
   */
  public void testGetProperties() {
    final Collection<CMakeProperty> cmds = CMakeProperties.getCommands();
    assertNotNull(cmds);
    assertTrue(cmds.size() > 1);
  }

  /**
   * Tests the variable collection access method.
   */
  public void testGetVariables() {
    final Collection<CMakeVariable> cmds = CMakeVariables.getCommands();
    assertNotNull(cmds);
    assertTrue(cmds.size() > 1);
  }

}
