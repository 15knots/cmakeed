/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui;

import junit.framework.TestCase;

import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Tests the message access class.
 */
@SuppressWarnings("nls")
public class MessagesTest extends TestCase
{
    /**
     * Default constructor.
     */
    public MessagesTest()
    {
    }
    
    /**
     * Tests the getString method.
     */
    public void testGetString()
    {
        assertFalse(StringUtils.isBlank(Messages.getString("PreferencePage.Color")));
        assertEquals("!foobar!", Messages.getString("foobar"));
        
        try {
            Messages.getString(null);
        }
        catch (final IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
}
