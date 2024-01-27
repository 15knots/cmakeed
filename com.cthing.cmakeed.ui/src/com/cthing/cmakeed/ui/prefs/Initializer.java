/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * Copyright 2019 Martin Weber
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.prefs;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;

import com.cthing.cmakeed.ui.CMakeEditorPlugin;

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
        final IPreferenceStore store = CMakeEditorPlugin.getDefault().getPreferenceStore();
        ColorRegistry registry = null;
        if (PlatformUI.isWorkbenchRunning()) {
          registry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();
        }

        setThemeBasedDefault(store, registry, Preferences.CMAKE_PROPERTY);
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_PROPERTY), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.CMAKE_RESERVED_WORD);
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_RESERVED_WORD), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.CMAKE_USER_VARIABLE);
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_USER_VARIABLE), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.CMAKE_VARIABLE);
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_VARIABLE), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.COMMAND);
        store.setDefault(Preferences.getStyleKey(Preferences.COMMAND), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.COMMENT);
        store.setDefault(Preferences.getStyleKey(Preferences.COMMENT), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.DEP_COMMAND);
        store.setDefault(Preferences.getStyleKey(Preferences.DEP_COMMAND),
                TextAttribute.STRIKETHROUGH);

        setThemeBasedDefault(store, registry, Preferences.STRING);
        store.setDefault(Preferences.getStyleKey(Preferences.STRING), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.DOLLAR_VARIABLE);
        store.setDefault(Preferences.getStyleKey(Preferences.DOLLAR_VARIABLE), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.MATCHING_BRACKETS_COLOR);

        store.setDefault(Preferences.MATCHING_BRACKETS_ON, true);
    }

    /**
     * Sets the default value based on the theme.
     *
     * @param store
     *                 the preference store
     * @param basicKey
     *                 the basic preference key
     */
    private static void setThemeBasedDefault(IPreferenceStore store, ColorRegistry registry, String basicKey) {
      String colorKey = Preferences.getColorKey(basicKey);
      String regKey = CMakeEditorPlugin.PLUGIN_ID + "." + colorKey;
      PreferenceConverter.setDefault(store, colorKey, findRGB(registry, regKey));
    }

    /**
     * Returns the RGB for the given key in the given color registry.
     *
     * @param registry
     *                   the color registry
     * @param key
     *                   the key for the constant in the registry
     * @return RGB the RGB
     */
    private static RGB findRGB(ColorRegistry registry, String key) {
      if (registry == null)
        return null;
      return registry.getRGB(key);
    }
}
