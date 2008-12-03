/* *****************************************************************************
 * Copyright 2008 BlueQuartz Software, Michael Jackson
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.properties;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import com.cthing.cmakeed.core.CorePlugin;
import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Provides CMake command information.
 */
public final class CMakeProperties
{
    private static final String PROPERTIES_EXT_ID = "properties";  //$NON-NLS-1$
    
    private static final String ATT_NAME = "name";  //$NON-NLS-1$
    private static final String ATT_DESC = "desc";  //$NON-NLS-1$
    private static final String ATT_DEPRECATED = "deprecated";  //$NON-NLS-1$
  //  private static final String ATT_VALUE = "value";  //$NON-NLS-1$
        
    private static Map<String, CMakeProperty> properties;
    
    /**
     * Not to be instantiated.
     */
    private CMakeProperties()
    {
    }
    
    /**
     * Loads the command information from the plugin extension point.
     */
    private static void loadCommands()
    {
        properties = new LinkedHashMap<String, CMakeProperty>();
        
        final IExtensionPoint extensionPoint =
            Platform.getExtensionRegistry().getExtensionPoint(CorePlugin.PLUGIN_ID, PROPERTIES_EXT_ID);
        final IExtension[] extensions = extensionPoint.getExtensions();
        
        for (IExtension extension : extensions) {
            if (extension.isValid()) {
                final IConfigurationElement[] commandElts =
                    extension.getConfigurationElements();
                
                for (IConfigurationElement commandElt : commandElts) {
                    final String name = commandElt.getAttribute(ATT_NAME);
                    final String desc = commandElt.getAttribute(ATT_DESC);
                    final Boolean deprecated = Boolean.valueOf(commandElt.getAttribute(ATT_DEPRECATED));

                    final CMakeProperty cmd = new CMakeProperty(name, desc, deprecated);
                   // System.out.println(cmd.toString());
                    properties.put(name, cmd);
                }
            }
        }
    }
    
    /**
     * Obtains information for the specified command.
     * 
     * @param name  Name of the command (case insensitive).
     * @return Command information object.
     */
    public static CMakeProperty getCommand(final String name)
    {
        if (properties == null) {
            loadCommands();
        }
        
        return (StringUtils.isBlank(name)) ? null : properties.get(name.toUpperCase());
    }
    
    /**
     * Provides all commands.
     * 
     * @return A collection containing all commands.
     */
    public static Collection<CMakeProperty> getCommands()
    {
        if (properties == null) {
            loadCommands();
        }
        
        return properties.values();
    }
}
