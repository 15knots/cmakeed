/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.text.rules.ITokenScanner;

import junit.framework.TestCase;

/**
 * Tests the CMakeScannerMgr class.
 */
public class CMakeScannerMgrTest extends TestCase
{
    /**
     * Default constructor for the class.
     */
    public CMakeScannerMgrTest()
    {
    }
    
    /**
     * Tests the getScanner method.
     */
    public void testGetScanner()
    {
        final CMakeScannerMgr mgr = new CMakeScannerMgr(new ColorMgr());
        
        for (String contentType : CMakePartitionScanner.CMAKE_CONTENT_TYPES) {
            final ITokenScanner scanner = mgr.getScanner(contentType);
            assertNotNull(scanner);
        }
    }
}
