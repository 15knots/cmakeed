/* *****************************************************************************
 * Copyright 2008 BlueQuartz Software, Michael Jackson
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.variables;

import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Represents information about a CMake command.
 */
public class CMakeVariable
{
    private String name;
    private String description;
    private boolean deprecated;

    
    /**
     * Constructor for the class.
     * 
     * @param name  Name of the command
     * @param description  Description of the command
     * @param deprecated  <code>true</code> if the command has been deprecated.
     */
    public CMakeVariable(final String name, final String description,
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
        
        final CMakeVariable other = (CMakeVariable)obj;
        
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
