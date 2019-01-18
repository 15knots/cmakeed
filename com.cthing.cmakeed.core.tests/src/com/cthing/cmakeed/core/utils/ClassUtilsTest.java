/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.utils;

import junit.framework.TestCase;

/**
 * Tests the CLassUtils utility class.
 */
@SuppressWarnings("nls")
public class ClassUtilsTest extends TestCase
{
    /**
     * Default constructor.
     */
    public ClassUtilsTest()
    {
    }

    /**
     * Tests the getShortClassName method.
     */
    public void testShortClassName()
    {
        assertEquals("ClassUtilsTest", ClassUtils.getShortClassName(getClass()));
        assertEquals("String", ClassUtils.getShortClassName(String.class));
        assertEquals("[null]", ClassUtils.getShortClassName((Class<?>)null));
        assertEquals("List", ClassUtils.getShortClassName("java.util.List"));
        assertEquals("List", ClassUtils.getShortClassName("List"));
        assertEquals("[null]", ClassUtils.getShortClassName((String)null));
    }

    /**
     * Tests the getMethodName methods.
     */
    public void testMethodName()
    {
        assertEquals("testMethodName", ClassUtils.getMethodName());
        assertEquals("testMethodName", ClassUtils.getMethodName(1));
    }
}

