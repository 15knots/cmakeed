/* *****************************************************************************
 * Copyright 2008 BlueQuartz Software, Michael Jackson
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core.uservariables;


import com.cthing.cmakeed.core.utils.StringUtils;

/**
 * Represents information about a User Defined Variable.
 */
public class CMakeUserVariable
{
    private String name;

    
    /**
     * Constructor for the class.
     * 
     * @param name  Name of the command
     * @param description  Description of the command
     * @param deprecated  <code>true</code> if the command has been deprecated.
     */
    public CMakeUserVariable(final String name)
    {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be blank."); //$NON-NLS-1$
        }
        
        this.name = name;
        
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
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
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
                ((this.name == null) ? 0 : this.name.hashCode());
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
        
        final CMakeUserVariable other = (CMakeUserVariable)obj;
        
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name) ) {
            return false;
        }
        
        return true;
    }

}
