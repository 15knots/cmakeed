/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.text.IDocument;

import com.cthing.cmakeed.ui.EditorTestCase;

/**
 * Tests the CMakePartitionScanner class.
 */
public class CMakePartitionScannerTest extends EditorTestCase
{
    /**
     * Default constructor for the class.
     */
    public CMakePartitionScannerTest()
    {
    }
    
    /**
     * Tests the content types array.
     */
    public void testContentTypes()
    {
        final String[] contentTypes = CMakePartitionScanner.CONTENT_TYPES;
        assertNotNull(contentTypes);
        assertTrue(contentTypes.length > 0);
    }
    
    /**
     * Tests that the specified content type represents the default partition.
     */
    public void testIsDefault()
    {
        assertTrue(CMakePartitionScanner.isDefault(IDocument.DEFAULT_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isDefault(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isDefault(null));
    }
    
    /**
     * Tests that the specified content type represents a comment partition.
     */
    public void testIsComment()
    {
        assertTrue(CMakePartitionScanner.isComment(CMakePartitionScanner.COMMENT_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isComment(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isComment(null));
    }
    
    /**
     * Tests that the specified content type represents a variable partition.
     */
    public void testIsVariable()
    {
        assertTrue(CMakePartitionScanner.isVariable(CMakePartitionScanner.VARREF_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isVariable(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isVariable(null));
    }
    
    /**
     * Tests that the specified content type represents a string partition.
     */
    public void testIsString()
    {
        assertTrue(CMakePartitionScanner.isString(CMakePartitionScanner.STRING_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isString(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isString(null));
    }
    
    /**
     * Tests that the specified content type represents either a regular
     * command or a deprecated command partition.
     */
    public void testIsAnyCommand()
    {
        assertTrue(CMakePartitionScanner.isAnyCommand(CMakePartitionScanner.COMMAND_CONTENT_TYPE));
        assertTrue(CMakePartitionScanner.isAnyCommand(CMakePartitionScanner.DEP_COMMAND_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isAnyCommand(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isAnyCommand(null));
    }
    
    /**
     * Tests that the specified content type represents a command partition.
     */
    public void testIsCommand()
    {
        assertTrue(CMakePartitionScanner.isCommand(CMakePartitionScanner.COMMAND_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isCommand(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isCommand(null));
    }
    
    /**
     * Tests that the specified content type represents a deprecated command
     * partition.
     */
    public void testIsDepCommand()
    {
        assertTrue(CMakePartitionScanner.isDepCommand(CMakePartitionScanner.DEP_COMMAND_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isDepCommand(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isDepCommand(null));
    }
    
    /**
     * Tests that the specified content type represents an arguments open
     * partition.
     */
    public void testIsArgsOpen()
    {
        assertTrue(CMakePartitionScanner.isArgsOpen(CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isArgsOpen(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isArgsOpen(null));
    }
    
    /**
     * Tests that the specified content type represents an arguments close
     * partition.
     */
    public void testIsArgsClose()
    {
        assertTrue(CMakePartitionScanner.isArgsClose(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isArgsClose(CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE));
        assertFalse(CMakePartitionScanner.isArgsClose(null));
    }
}
