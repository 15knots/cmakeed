/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.prefs;

import com.cthing.cmakeed.ui.Messages;

/**
 * Constants representing preferences.
 */
public final class Preferences
{
    /** Use UPPER CASE for CMake commands */
    public static final String UPPER_CASE_COMMANDS = "upperCaseCommands";      //$NON-NLS-1$
    /** Use spaces instead of tabs key.
     * @deprecated use {@link org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants} instead
     */
    @Deprecated
    public static final String SPACES_FOR_TABS = "spacesForTabsPref";      //$NON-NLS-1$
    /** CMake command text descriptor key. */
    public static final String COMMAND = "command";             //$NON-NLS-1$
    /** Deprecated CMake command text descriptor key. */
    public static final String DEP_COMMAND = "depCommand";      //$NON-NLS-1$
    /** Command text descriptor key. */
    public static final String COMMENT = "comment";             //$NON-NLS-1$
    /** String text descriptor key. */
    public static final String STRING = "string";               //$NON-NLS-1$
    /** CMake variable text descriptor key. */
    public static final String DOLLAR_VARIABLE = "variable";           //$NON-NLS-1$
    /** CMake Variable text descriptor key. */
    public static final String CMAKE_VARIABLE = "cmake_variable";           //$NON-NLS-1$
    /** CMake Property text descriptor key. */
    public static final String CMAKE_PROPERTY = "cmake_property";           //$NON-NLS-1$
    /** CMake Reserved Word text descriptor key. */
    public static final String CMAKE_RESERVED_WORD = "cmake_reserved_word";           //$NON-NLS-1$
    /** CMake User Variable text descriptor key. */
    public static final String CMAKE_USER_VARIABLE = "cmake_user_variable";
    /** Text color sub key. */
    public static final String COLOR = "Color";     //$NON-NLS-1$
    /** Text style sub key. */
    public static final String STYLE = "Style";     //$NON-NLS-1$

    /** All text attribute related keys.  */
    public static final String[] TEXT_KEYS = new String[] {
        COMMAND,
        DEP_COMMAND,
        COMMENT,
        STRING,
        DOLLAR_VARIABLE,
        CMAKE_VARIABLE,
        CMAKE_PROPERTY,
        CMAKE_RESERVED_WORD,
        CMAKE_USER_VARIABLE,
    };

    /**
     * Not to be instantiated.
     */
    private Preferences()
    {
    }

    /**
     * Forms the color sub key for a text attribute.
     *
     * @param baseKey  Base text attribute key.
     * @return Color sub key.
     */
    public static String getColorKey(final Object baseKey)
    {
        return baseKey + COLOR;
    }

    /**
     * Forms the style sub key for a text attribute.
     *
     * @param baseKey  Base text attribute key.
     * @return Style sub key.
     */
    public static String getStyleKey(final Object baseKey)
    {
        return baseKey + STYLE;
    }

    /**
     * Obtains the message key for the specified preference.
     *
     * @param baseKey  Base preference key.
     * @return Message key
     */
    public static String getMessage(final Object baseKey)
    {
        return Messages.getString("Preference." + baseKey);     //$NON-NLS-1$
    }

    /**
     * Tests whether the specified preference is a text preference.
     *
     * @param preference  Preference key to test
     * @return <code>true</code> if the specified preference key is a
     *      text preference.
     */
    public static boolean isTextPreference(final String preference)
    {
        for (String baseKey : TEXT_KEYS) {
            if (getColorKey(baseKey).equals(preference) ||
                    getStyleKey(baseKey).equals(preference)) {
                return true;
            }
        }

        return false;
    }
}
