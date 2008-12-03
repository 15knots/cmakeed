/* *****************************************************************************
 * Copyright 2008 BlueQuartz Software, Michael Jackson
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.reservedwords;

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
public final class CMakeReservedWords
{
    private static final String PROPERTIES_EXT_ID = "reservedwords";  //$NON-NLS-1$
    
    private static final String ATT_NAME = "name";  //$NON-NLS-1$

        
    private static Map<String, CMakeReservedWord> reservedWords;
    
    /**
     * Not to be instantiated.
     */
    private CMakeReservedWords()
    {
    }
    
    /**
     * Loads the command information from the plugin extension point.
     */
    private static void loadCommands()
    {
        reservedWords = new LinkedHashMap<String, CMakeReservedWord>();
        
        final IExtensionPoint extensionPoint =
            Platform.getExtensionRegistry().getExtensionPoint(CorePlugin.PLUGIN_ID, PROPERTIES_EXT_ID);
        final IExtension[] extensions = extensionPoint.getExtensions();
        
        for (IExtension extension : extensions) {
            if (extension.isValid()) {
                final IConfigurationElement[] commandElts =
                    extension.getConfigurationElements();
                
                for (IConfigurationElement commandElt : commandElts) {
                    final String name = commandElt.getAttribute(ATT_NAME);
                   

                    final CMakeReservedWord cmd = new CMakeReservedWord(name);
                    reservedWords.put(name, cmd);
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
    public static CMakeReservedWord getCommand(final String name)
    {
        if (reservedWords == null) {
            loadCommands();
        }
        
        return (StringUtils.isBlank(name)) ? null : reservedWords.get(name.toUpperCase());
    }
    
    /**
     * Provides all commands.
     * 
     * @return A collection containing all commands.
     */
    public static Collection<CMakeReservedWord> getCommands()
    {
        if (reservedWords == null) {
            loadCommands();
        }
        
        return reservedWords.values();
    }
}
