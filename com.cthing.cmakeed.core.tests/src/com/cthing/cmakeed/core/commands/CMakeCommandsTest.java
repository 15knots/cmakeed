/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * Copyright 2017 Martin Weber
 *  * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Tests the CMakeCommands class.
 */
@SuppressWarnings("nls")
public class CMakeCommandsTest
{

    /**
     * Tests the command collection access method.
     */
    @Test
    @Ignore("Tested in plugin de.mare.cmakeed.assist.test")
    public void testGetCommands()
    {
        final Collection<CMakeCommand> cmds = CMakeCommands.getCommands();
        assertNotNull(cmds);
        assertTrue(cmds.size() > 1);
    }

    /**
     * Tests the command access method.
     */
    @Test
    @Ignore("Tested in plugin de.mare.cmakeed.assist.test")
    public void testGetCommand()
    {
        final CMakeCommand cmd1 = CMakeCommands.getCommand("SET");
        assertNotNull(cmd1);
        assertEquals("SET", cmd1.getName().toUpperCase());
        assertFalse(StringUtils.isBlank(cmd1.getDescription()));
        assertFalse(cmd1.isDeprecated());

        final CMakeCommand cmd2 = CMakeCommands.getCommand("set");
        assertNotNull(cmd2);

        assertEquals(cmd1, cmd2);

        assertNull(CMakeCommands.getCommand(null));
    }
}
