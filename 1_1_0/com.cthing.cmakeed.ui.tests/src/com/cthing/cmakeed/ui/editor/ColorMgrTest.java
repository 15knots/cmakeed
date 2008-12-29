/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import junit.framework.TestCase;

/**
 * Tests the color manager class.
 */
public class ColorMgrTest extends TestCase
{
    /**
     * Default constructor for the test.
     */
    public ColorMgrTest()
    {
    }
    
    /**
     * Tests the getColor method.
     */
    public void testGetColor()
    {
        final ColorMgr mgr = new ColorMgr();
        
        final RGB rgb1 = new RGB(10, 20, 30);
        final Color color1 = mgr.getColor(rgb1);
        assertEquals(10, color1.getRed());
        assertEquals(20, color1.getGreen());
        assertEquals(30, color1.getBlue());
    }
}
