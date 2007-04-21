/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.commands;

import java.util.ArrayList;
import java.util.List;

import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Represents information about a CMake command.
 */
public class CMakeCommand
{
    private String name;
    private String description;
    private boolean deprecated;
    private List<String> usages;
    
    /**
     * Constructor for the class.
     * 
     * @param name  Name of the command
     * @param description  Description of the command
     * @param deprecated  <code>true</code> if the command has been
     *      deprecated.
     */
    public CMakeCommand(final String name, final String description,
                        final boolean deprecated)
    {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be blank."); //$NON-NLS-1$
        }
        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("description cannot be blank."); //$NON-NLS-1$
        }
        
        this.name = name;
        this.description = description;
        this.deprecated = deprecated;
        this.usages = new ArrayList<String>();
    }
    
    /**
     * Adds the specified usage string to the command.
     * 
     * @param usage  Command usage string.
     */
    public void addUsage(final String usage)
    {
        if (StringUtils.isBlank(usage)) {
            throw new IllegalArgumentException("usage cannot be blank."); //$NON-NLS-1$
        }

        this.usages.add(usage);
    }

    /**
     * Indicates whether the command has been deprecated.
     * 
     * @return <code>true</code> if the command has been deprecated.
     */
    public boolean isDeprecated()
    {
        return this.deprecated;
    }

    /**
     * Provides the name of the command.
     * 
     * @return The command name.
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Provides a description for the command.
     * 
     * @return Command description.
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /**
     * Obtains the usage strings for the command.
     * 
     * @return Usage strings for the command.
     */
    public String[] getUsages()
    {
        return this.usages.toArray(new String[this.usages.size()]);
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name.toUpperCase();
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        return prime +
                ((this.name == null) ? 0 : this.name.toUpperCase().hashCode());
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        
        final CMakeCommand other = (CMakeCommand)obj;
        
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.toUpperCase().equals(other.name.toUpperCase())) {
            return false;
        }
        
        return true;
    }
}
