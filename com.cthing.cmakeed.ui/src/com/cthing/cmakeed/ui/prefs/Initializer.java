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
        if (PlatformUI.isWorkbenchRunning())
          registry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();

        setThemeBasedDefault(store, registry, Preferences.CMAKE_PROPERTY, new RGB(0, 127, 85));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_PROPERTY), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.CMAKE_RESERVED_WORD, new RGB(127, 85, 85));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_RESERVED_WORD), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.CMAKE_USER_VARIABLE, new RGB(52,89,129));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_USER_VARIABLE), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.CMAKE_VARIABLE, new RGB(127, 127, 85));
        store.setDefault(Preferences.getStyleKey(Preferences.CMAKE_VARIABLE), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.COMMAND, new RGB(0, 0, 128));
        store.setDefault(Preferences.getStyleKey(Preferences.COMMAND), SWT.NONE);

        setThemeBasedDefault(store, registry,Preferences.COMMENT, new RGB(0, 128, 0));
        store.setDefault(Preferences.getStyleKey(Preferences.COMMENT), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.DEP_COMMAND, new RGB(0, 0, 128));
        store.setDefault(Preferences.getStyleKey(Preferences.DEP_COMMAND),
                TextAttribute.STRIKETHROUGH);


        setThemeBasedDefault(store, registry, Preferences.STRING, new RGB(42, 0, 255));
        store.setDefault(Preferences.getStyleKey(Preferences.STRING), SWT.NONE);

        setThemeBasedDefault(store, registry, Preferences.DOLLAR_VARIABLE, new RGB(52,89,129));
        store.setDefault(Preferences.getStyleKey(Preferences.DOLLAR_VARIABLE), SWT.NONE);

        store.setDefault(Preferences.MATCHING_BRACKETS_ON, true);
        store.setDefault(Preferences.MATCHING_BRACKETS_COLOR, "254,222,0");
    }

    /**
     * Sets the default value based on the theme.
     *
     * @param store
     *                 the preference store
     * @param basicKey
     *                 the basic preference key
     * @param newValue
     *                 the default value
     */
    private static void setThemeBasedDefault(IPreferenceStore store, ColorRegistry registry, String basicKey,
        RGB newValue) {
      String colorKey = Preferences.getColorKey(basicKey);
      String regKey = CMakeEditorPlugin.PLUGIN_ID + "." + colorKey;
      PreferenceConverter.setDefault(store, colorKey, findRGB(registry, regKey, newValue));
    }

    /**
     * Returns the RGB for the given key in the given color registry.
     *
     * @param registry
     *                   the color registry
     * @param key
     *                   the key for the constant in the registry
     * @param defaultRGB
     *                   the default RGB if no entry is found
     * @return RGB the RGB
     */
    private static RGB findRGB(ColorRegistry registry, String key, RGB defaultRGB) {
      if (registry == null)
        return defaultRGB;

      RGB rgb = registry.getRGB(key);
      return rgb != null ? rgb : defaultRGB;
    }
  }
