/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.prefs;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import com.cthing.cmakeed.ui.UIPlugin;

/**
 * Initializes the default preference values.
 */
public class Initializer extends AbstractPreferenceInitializer
{
    /**
     * Default constructor for the class.
     */
    public Initializer()
    {
    }
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences()
    {
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(Preferences.SPACES_FOR_TABS, true);
        
        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.COMMAND), new RGB(0, 0, 128));
        store.setDefault(Preferences.getStyleKey(Preferences.COMMAND), SWT.NONE);

        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.DEP_COMMAND), new RGB(0, 0, 128));
        store.setDefault(Preferences.getStyleKey(Preferences.DEP_COMMAND),
                TextAttribute.STRIKETHROUGH);

        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.COMMENT), new RGB(0, 128, 0));
        store.setDefault(Preferences.getStyleKey(Preferences.COMMENT), SWT.NONE);

        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.STRING), new RGB(42, 0, 255));
        store.setDefault(Preferences.getStyleKey(Preferences.STRING), SWT.NONE);

        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.DOLLAR_VARIABLE), new RGB(52,89,129));
        store.setDefault(Preferences.getStyleKey(Preferences.DOLLAR_VARIABLE), SWT.NONE);
        
        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.CMAKE_VARIABLE), new RGB(127, 127, 85));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_VARIABLE), SWT.NONE);
        
        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.CMAKE_PROPERTY), new RGB(0, 127, 85));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_PROPERTY), SWT.NONE);
        
        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.CMAKE_RESERVED_WORD), new RGB(127, 85, 85));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_RESERVED_WORD), SWT.NONE);
    
        PreferenceConverter.setDefault(store,
                Preferences.getColorKey(Preferences.CMAKE_USER_VARIABLE), new RGB(52,89,129));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_USER_VARIABLE), SWT.NONE);
    
    }
}
