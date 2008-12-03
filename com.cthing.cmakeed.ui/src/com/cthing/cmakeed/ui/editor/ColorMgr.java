/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Manages the color table for the editor.
 */
public class ColorMgr
{
    /** Colors for the text. */
    private Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);

    /**
     * Default constructor for the class.
     */
    public ColorMgr()
    {
    }
    
    /**
     * Cleans up the class when it is no longer needed.
     */
    public void dispose()
    {
        for (Color c : this.fColorTable.values()) {
            c.dispose();
        }
    }
    
    /**
     * Obtains the color corresponding to the specified RGB value.
     * 
     * @param rgb  RGB value for which a color is desired.
     * @return Color corresponding to the specified RGB value.
     */
    public Color getColor(final RGB rgb)
    {
        Color color = this.fColorTable.get(rgb);
        if (color == null) {
            color = new Color(Display.getCurrent(), rgb);
            this.fColorTable.put(rgb, color);
        }
        return color;
    }
}
