/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.commands;

import junit.framework.TestCase;

/**
 * Tests the CMakeCommand class.
 */
@SuppressWarnings("nls")
public class CMakeCommandTest extends TestCase
{
    private static final String NAME = "SUBDIRS";
    private static final String LOWER_NAME = "subdirs";
    private static final String NAME2 = "SET";
    private static final String DESC = "Visits a sub directory";
    private static final boolean DEPRECATED = true;
    
    /**
     * Default constructor for the class.
     */
    public CMakeCommandTest()
    {
    }
    
    /**
     * {@inheritDoc}
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
    }

    /**
     * Tests the properties of the class set in the constructor.
     */
    public void testCtorProperties()
    {
        final CMakeCommand cmd = new CMakeCommand(NAME, DESC, DEPRECATED);
        assertEquals(NAME, cmd.getName());
        assertEquals(DESC, cmd.getDescription());
        assertTrue(cmd.isDeprecated());
        
        try {
            new CMakeCommand(null, DESC, DEPRECATED);
            fail("Constructor did not throw with a null name.");
        }
        catch (final IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
        
        try {
            new CMakeCommand("  ", DESC, DEPRECATED);
            fail("Constructor did not throw with a blank name.");
        }
        catch (final IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
        
        try {
            new CMakeCommand(NAME, null, DEPRECATED);
            fail("Constructor did not throw with a null description.");
        }
        catch (final IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    /**
     * Tests the usage string property.
     */
    public void testUsage()
    {
        final CMakeCommand cmd = new CMakeCommand(NAME, DESC, DEPRECATED);

        assertEquals(0, cmd.getUsages().length);
        
        cmd.addUsage("(var value)");
        cmd.addUsage("(ENV{var} value)");
        assertEquals(2, cmd.getUsages().length);
        
        try {
            cmd.addUsage(null);
            fail("addUsage did not throw with a null value.");
        }
        catch (final IllegalArgumentException e) {
        }
        
        try {
            cmd.addUsage("");
            fail("addUsage did not throw with an empty string.");
        }
        catch (final IllegalArgumentException e) {
        }
    }
    
    /**
     * Tests the toString method.
     */
    public void testToString()
    {
        final CMakeCommand cmd = new CMakeCommand(NAME, DESC, DEPRECATED);
        assertEquals(NAME, cmd.toString());
    }
    
    /**
     * Tests the equals and hashCode methods.
     */
    public void testEquality()
    {
        final CMakeCommand cmd1 = new CMakeCommand(NAME, DESC, DEPRECATED);
        final CMakeCommand cmd2 = new CMakeCommand(LOWER_NAME, DESC, DEPRECATED);
        final CMakeCommand cmd3 = new CMakeCommand(NAME2, DESC, DEPRECATED);
        
        assertEquals(cmd1, cmd1);
        assertEquals(cmd1, cmd2);
        assertFalse(cmd1.equals(cmd3));
        assertFalse(cmd1.equals(null));
        assertFalse(cmd1.equals(new Object()));
        
        assertEquals(cmd1.hashCode(), cmd2.hashCode());
        assertFalse(cmd1.hashCode() == cmd3.hashCode());
    }
}
