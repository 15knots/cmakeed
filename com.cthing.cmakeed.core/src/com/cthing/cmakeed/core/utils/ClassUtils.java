/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.utils;

/**
 * Utility functions for dealing with classes.
 */
public final class ClassUtils
{
    /**
     * Not to be instantiated.
     */
    private ClassUtils()
    {
    }

    /**
     * Extracts the last component of a fully qualified class name.
     *
     * @param cls  Class whose short name is desired.
     * @return The last component of the fully qualified class name.
     */
    public static String getShortClassName(final java.lang.Class<?> cls)
    {
        if (cls == null) {
            return "[null]";    //$NON-NLS-1$
        }

        return getShortClassName(cls.getName());
    }

    /**
     * Extracts the last component of a fully qualified class name.
     *
     * @param className  Class whose short name is desired.
     * @return The last component of the fully qualified class name.
     */
    public static String getShortClassName(final String className)
    {
        if (className == null) {
            return "[null]";    //$NON-NLS-1$
        }

        final int idx = className.lastIndexOf('.');
        return (idx == -1) ? className : className.substring(idx + 1);
    }

    /**
     * Returns the name of the method calling this method.
     *
     * @return Name of the method that is calling this method.
     */
    public static String getMethodName()
    {
        return getMethodName(2);
    }

    /**
     * Returns the method name for the specified location in the current thread's
     * call stack. This method is useful if you want the name of a method
     * somewhere above you in the call stack.
     *
     * @param callStackOffset The name of a method is determined relative to
     *      the current thread's call stack. Pass zero to get the name of the
     *      method calling <code>getMethodName</code>. To get the name of the
     *      method calling the method that is calling <code>getMethodName</code>,
     *      pass one.
     * @return Name of the method.
     */
    public static String getMethodName(final int callStackOffset)
    {
        return new Exception().getStackTrace()[callStackOffset].getMethodName();
    }
}

