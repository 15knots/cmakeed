/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cthing.cmakeed.core.commands.CMakeCommandTest;
import com.cthing.cmakeed.core.commands.CMakeCommandsTest;
import com.cthing.cmakeed.core.utils.ClassUtilsTest;
import com.cthing.cmakeed.core.utils.StringUtilsTest;

/**
 * Runs all tests for the CMake editor core plug-in.
 */
public class CoreTestSuite extends TestSuite
{
    /**
     * Default constructor.
     */
    public CoreTestSuite()
    {
        // com.cthing.cmakeed.core.utils
        addTestSuite(StringUtilsTest.class);
        addTestSuite(ClassUtilsTest.class);
        
        // com.cthing.cmakeed.core.commands
        addTestSuite(CMakeCommandTest.class);
        addTestSuite(CMakeCommandsTest.class);
    }

    /**
     * Returns an instance of the test suite. This is required to use the JUnit
     * Launcher.
     * 
     * @return JUnit test suite.
     */
    public static Test suite()
    {
        return new CoreTestSuite();
    }
}
