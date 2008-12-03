/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.commands;

import java.util.Collection;

import junit.framework.TestCase;

import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Tests the CMakeCommands class.
 */
@SuppressWarnings("nls")
public class CMakeCommandsTest extends TestCase
{
    /**
     * Default constructor for the class.
     */
    public CMakeCommandsTest()
    {
    }
    
    /**
     * Tests the command collection access method.
     */
    public void testGetCommands()
    {
        final Collection<CMakeCommand> cmds = CMakeCommands.getCommands();
        assertNotNull(cmds);
        assertTrue(cmds.size() > 1);
    }
    
    /**
     * Tests the command access method.
     */
    public void testGetCommand()
    {
        final CMakeCommand cmd1 = CMakeCommands.getCommand("SET");
        assertNotNull(cmd1);
        assertEquals("SET", cmd1.getName());
        assertFalse(StringUtils.isBlank(cmd1.getDescription()));
        assertFalse(cmd1.isDeprecated());
        
        final CMakeCommand cmd2 = CMakeCommands.getCommand("set");
        assertNotNull(cmd2);
        
        assertEquals(cmd1, cmd2);
        
        assertNull(CMakeCommands.getCommand(null));
    }
}
