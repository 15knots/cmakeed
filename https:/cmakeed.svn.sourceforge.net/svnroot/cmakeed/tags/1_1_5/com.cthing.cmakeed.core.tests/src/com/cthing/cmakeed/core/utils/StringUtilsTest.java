/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.utils;

import junit.framework.TestCase;

/**
 * Tests the StringUtils utility class.
 */
@SuppressWarnings("nls")
public class StringUtilsTest extends TestCase
{
    /**
     * Default constructor for the test.
     */
    public StringUtilsTest()
    {
    }
    
    /**
     * Tests the isBlank method.
     */
    public void testIsBlank()
    {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("  \t  "));
        assertFalse(StringUtils.isBlank("asdf"));
        assertFalse(StringUtils.isBlank("   asdf   "));
    }
}

