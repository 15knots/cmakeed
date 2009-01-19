/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.utils;

/**
 * Utility methods for working with strings.
 */
public final class StringUtils
{
    /**
     * Not to be instantiated.
     */
    private StringUtils()
    {
    }
    
    /**
     * Indicates whether the specified string is <code>null</code> or contains
     * only whitespace.
     * 
     * @param str  String to test
     * @return <code>true</code> if the specified string is either
     *      <code>null</code> or contains only whitespace.
     */
    public static boolean isBlank(final String str)
    {
        return (str == null) || (str.trim().length() == 0);
    }
}

