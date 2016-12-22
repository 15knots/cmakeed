/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
public final class CMakeCommands
{
    private static final String COMMANDS_EXT_ID = "commands";  //$NON-NLS-1$

    private static final String ATT_NAME = "name";  //$NON-NLS-1$
    private static final String ATT_DESC = "desc";  //$NON-NLS-1$
    private static final String ATT_DEPRECATED = "deprecated";  //$NON-NLS-1$
    private static final String ATT_VALUE = "value";  //$NON-NLS-1$

    private static Map<String, CMakeCommand> commands;

    private static List<CMakeCommand> commandsSorted ;

    /**
     * Not to be instantiated.
     */
    private CMakeCommands()
    {
    }

    /**
     * Loads the command information from the plugin extension point.
     */
    private static void loadCommands()
    {
        commands = new HashMap<String, CMakeCommand>();
        
        final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CorePlugin.PLUGIN_ID, COMMANDS_EXT_ID);
        final IExtension[] extensions = extensionPoint.getExtensions();

        for (IExtension extension : extensions) {
            if (extension.isValid()) {
                final IConfigurationElement[] commandElts = extension.getConfigurationElements();

                for (IConfigurationElement commandElt : commandElts) {
                    final String name = commandElt.getAttribute(ATT_NAME);
                    final String desc = commandElt.getAttribute(ATT_DESC);
                    final Boolean deprecated = Boolean.valueOf(commandElt.getAttribute(ATT_DEPRECATED));

                    CMakeCommand cmd= commands.get(name);
                    if(cmd==null){
                        cmd = new CMakeCommand(name, desc, deprecated);
                        commands.put(name, cmd);
                    }
                    //System.out.println(cmd.toString());

                    final IConfigurationElement[] usageElts = commandElt.getChildren();
                    for (IConfigurationElement usageElt : usageElts) {
                        cmd.addUsage(usageElt.getAttribute(ATT_VALUE));
                    }
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
    public static CMakeCommand getCommand(final String name)
    {
        if (commands == null) {
            loadCommands();
        }

        return (StringUtils.isBlank(name)) ? null : commands.get(name.toLowerCase());
    }

    /**
     * Provides all commands. The returned collection is guaranteed to be sorted first by command
     * name and secondly by usage description text.
     *
     * @return A collection containing all commands.
     */
    public static Collection<CMakeCommand> getCommands()
    {
        if (commands == null) {
            loadCommands();
        }
        if (commandsSorted == null) {
            // sort commands by name and usage text..
            commandsSorted = new ArrayList<CMakeCommand>(commands.values());
            Collections.sort(commandsSorted, new Comparator<CMakeCommand>() {

              public int compare(CMakeCommand c1, CMakeCommand c2) {
                final int byName = c1.getName().compareToIgnoreCase(c2.getName());
                return byName!=0 ? byName: c1.getUsages()[0].compareToIgnoreCase(c2.getUsages()[0]);
              }
            });
        }

        return commandsSorted;
    }
}
