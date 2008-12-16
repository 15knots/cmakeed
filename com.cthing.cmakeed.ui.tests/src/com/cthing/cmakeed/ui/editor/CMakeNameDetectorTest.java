/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import junit.framework.TestCase;

/**
 * Tests the CMakeNameDetector class.
 */
public class CMakeNameDetectorTest extends TestCase
{
    /**
     * Default constructor for the test.
     */
    public CMakeNameDetectorTest()
    {
    }
    
    /**
     * Tests the isWordStart method.
     */
    public void testIsWordStart()
    {
        final CMakeNameDetector detector = new CMakeNameDetector();
        
        assertTrue(detector.isWordStart('a'));
        assertTrue(detector.isWordStart('A'));
        assertTrue(detector.isWordStart('7'));
        assertTrue(detector.isWordStart('/'));
        assertTrue(detector.isWordStart('_'));
        assertTrue(detector.isWordStart('.'));
        assertTrue(detector.isWordStart('+'));
        assertTrue(detector.isWordStart('-'));
        
        assertFalse(detector.isWordStart(' '));
        assertFalse(detector.isWordStart('*'));
    }
    
    /**
     * Tests the isWordPart method.
     */
    public void testIsWordPart()
    {
        final CMakeNameDetector detector = new CMakeNameDetector();
        
        assertTrue(detector.isWordPart('a'));
        assertTrue(detector.isWordPart('A'));
        assertTrue(detector.isWordPart('7'));
        assertTrue(detector.isWordPart('/'));
        assertTrue(detector.isWordPart('_'));
        assertTrue(detector.isWordPart('.'));
        assertTrue(detector.isWordPart('+'));
        assertTrue(detector.isWordPart('-'));
        
        assertFalse(detector.isWordPart(' '));
        assertFalse(detector.isWordPart('*'));
    }
}
