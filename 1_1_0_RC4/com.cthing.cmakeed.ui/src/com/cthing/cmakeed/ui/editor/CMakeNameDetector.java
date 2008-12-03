/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Tests whether a character is part of a CMake name.
 */
public class CMakeNameDetector implements IWordDetector
{
    /**
     * Default constructor for the class.
     */
    public CMakeNameDetector()
    {
    }
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
     */
    public boolean isWordPart(final char c)
    {
        return isNameChar(c);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
     */
    public boolean isWordStart(final char c)
    {
        return isNameChar(c);
    }

    /**
     * Tests whether the specified character is a valid CMake name character.
     * 
     * @param c  Character to test
     * @return <code>true</code> if the specified character is a valid CMake
     *      name character.
     */
    private boolean isNameChar(final char c)
    {
        return Character.isLetterOrDigit(c) ||
               (c == '/') ||
               (c == '_') ||
               (c == '.') ||
               (c == '+') ||
               (c == '-');
    }
}
