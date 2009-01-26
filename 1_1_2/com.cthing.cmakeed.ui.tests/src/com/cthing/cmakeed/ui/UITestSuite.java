/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cthing.cmakeed.ui.editor.CMakeCommandAssistTest;
import com.cthing.cmakeed.ui.editor.CMakeDocumentProviderTest;
import com.cthing.cmakeed.ui.editor.CMakeEditorConfigurationTest;
import com.cthing.cmakeed.ui.editor.CMakeNameDetectorTest;
import com.cthing.cmakeed.ui.editor.CMakePartitionScannerTest;
import com.cthing.cmakeed.ui.editor.CMakeScannerMgrTest;
import com.cthing.cmakeed.ui.editor.ColorMgrTest;
import com.cthing.cmakeed.ui.editor.EditorUtilsTest;
import com.cthing.cmakeed.ui.editor.rules.ArgsCloseRuleTest;
import com.cthing.cmakeed.ui.editor.rules.ArgsOpenRuleTest;
import com.cthing.cmakeed.ui.editor.rules.CMakeCommandRuleTest;

/**
 * Runs all tests for the CMake editor UI plug-in.
 */
public class UITestSuite extends TestSuite
{
    /**
     * Default constructor.
     */
    public UITestSuite()
    {
        // com.cthing.cmakeed.ui
        addTestSuite(MessagesTest.class);
        
        // com.cthing.cmakeed.ui.editor
        addTestSuite(EditorUtilsTest.class);
        addTestSuite(ColorMgrTest.class);
        addTestSuite(CMakeNameDetectorTest.class);
        addTestSuite(CMakeScannerMgrTest.class);
        addTestSuite(CMakeDocumentProviderTest.class);
        addTestSuite(CMakeEditorConfigurationTest.class);
        addTestSuite(CMakePartitionScannerTest.class);
        addTestSuite(CMakeCommandAssistTest.class);
        
        // com.cthing.cmakeed.ui.editor.rules
        addTestSuite(ArgsCloseRuleTest.class);
        addTestSuite(ArgsOpenRuleTest.class);
        addTestSuite(CMakeCommandRuleTest.class);
    }

    /**
     * Returns an instance of the test suite. This is required to use the JUnit
     * Launcher.
     * 
     * @return JUnit test suite.
     */
    public static Test suite()
    {
        return new UITestSuite();
    }
}
