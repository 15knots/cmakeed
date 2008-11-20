/* *****************************************************************************
 * Copyright 2008 BlueQuartz Software, Michael Jackson
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.uservariables;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Provides CMake command information.
 */
public final class CMakeUserVariables
{
       
    private static Map<String, CMakeUserVariable> uservariables;
    
    /**
     * Not to be instantiated.
     */
    private CMakeUserVariables()
    {
    }
    
    private static void initVariables()
    {
    	uservariables = new LinkedHashMap<String, CMakeUserVariable>();
    }
    
    /**
     * Obtains information for the specified command.
     * 
     * @param name  Name of the command (case insensitive).
     * @return Command information object.
     */
    public static CMakeUserVariable getUserVariable(final String name)
    {
        if (uservariables == null) {
        	initVariables();
        }
        return (StringUtils.isBlank(name)) ? null : uservariables.get(name);
    }
    
    /**
     * Adds a user defined variable to the list
     * @param name
     */
    public static void addUserVariable(final String name)
    {
        if (uservariables == null) {
        	initVariables();
        }
        final CMakeUserVariable var = new CMakeUserVariable(name);
        uservariables.put(name, var);
    }
    
    /**
     * Provides all commands.
     * 
     * @return A collection containing all commands.
     */
    public static Collection<CMakeUserVariable> getUserVariables()
    {        
    	if (uservariables == null) {
    		initVariables();
    	}
        return uservariables.values();
    }
}
