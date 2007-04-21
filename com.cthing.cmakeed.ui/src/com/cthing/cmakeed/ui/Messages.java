/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.cthing.cmakeed.core.utils.StringUtils;


/**
 * Message catalog access class.
 */
public final class Messages
{
    private static final String BUNDLE_NAME = "com.cthing.cmakeed.ui.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    /**
     * Default constructor for the class.
     */
    private Messages()
    {
    }

    /**
     * Obtains the message corresponding to the specified key.
     * 
     * @param key  Identifier for the desired message.
     * @return Message corresponding to the specified key.
     */
    public static String getString(final String key)
    {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key cannot be blank"); //$NON-NLS-1$
        }
        
        try {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (final MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    /**
     * Obtains the resource bundle for the plug-in's messages.
     * 
     * @return Resource bundle for the plug-in's messages.
     */
    public static ResourceBundle getResourceBundle()
    {
        return RESOURCE_BUNDLE;
    }
}
