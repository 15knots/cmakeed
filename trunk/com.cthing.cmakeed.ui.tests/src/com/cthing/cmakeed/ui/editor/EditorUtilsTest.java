/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import com.cthing.cmakeed.core.commands.CMakeCommand;
import com.cthing.cmakeed.ui.EditorTestCase;

/**
 * Tests the EditorUtils class.
 */
@SuppressWarnings("nls")
public class EditorUtilsTest extends EditorTestCase
{
    /**
     * Default constructor for the test.
     */
    public EditorUtilsTest()
    {
    }
    
    /**
     * Tests the inArguments method.
     */
    public void testInArguments()
    {
        assertTrue(EditorUtils.inArguments(getDocument(), 60));        
        assertFalse(EditorUtils.inArguments(getDocument(), 6));
        assertFalse(EditorUtils.inArguments(getDocument(), 50));
        assertFalse(EditorUtils.inArguments(getDocument(), 56));
        assertFalse(EditorUtils.inArguments(getDocument(), 77));
        assertFalse(EditorUtils.inArguments(getDocument(), 0));
        assertFalse(EditorUtils.inArguments(getDocument(), -1));
        assertFalse(EditorUtils.inArguments(getDocument(),
                                            getDocument().getLength() + 1));
    }
    
    /**
     * Tests the getCommandName methods.
     */
    public void testGetCommandName()
    {
        assertEquals("add_executable", EditorUtils.getCommandName(getViewer(), 50));
        assertNull(EditorUtils.getCommandName(getViewer(), 60));
        assertNull(EditorUtils.getCommandName(getViewer(), 77));
        assertNull(EditorUtils.getCommandName(getViewer(), 0));
    }
    
    /**
     * Tests the getCommand methods.
     */
    public void testGetCommand()
    {
        final CMakeCommand cmd = EditorUtils.getCommand(getViewer(), 50);
        assertEquals("ADD_EXECUTABLE", cmd.getName());
        assertNull(EditorUtils.getCommand(getViewer(), 60));
        assertNull(EditorUtils.getCommand(getViewer(), 77));
        assertNull(EditorUtils.getCommand(getViewer(), 0));
    }
    
    /**
     * Tests the findContainingCommand method.
     */
    public void testFindContainingCommand()
    {
        CMakeCommand cmd = EditorUtils.findContainingCommand(getDocument(), 60);
        assertEquals("ADD_EXECUTABLE", cmd.getName());

        cmd = EditorUtils.findContainingCommand(getDocument(), 50);
        assertEquals("ADD_EXECUTABLE", cmd.getName());

        cmd = EditorUtils.findContainingCommand(getDocument(), 56);
        assertEquals("ADD_EXECUTABLE", cmd.getName());

        cmd = EditorUtils.findContainingCommand(getDocument(), 57);
        assertEquals("ADD_EXECUTABLE", cmd.getName());

        cmd = EditorUtils.findContainingCommand(getDocument(), 76);
        assertNull(cmd);

        cmd = EditorUtils.findContainingCommand(getDocument(), 5);
        assertNull(cmd);

        cmd = EditorUtils.findContainingCommand(getDocument(), 0);
        assertNull(cmd);
    }
}
