/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.RGB;

import com.cthing.cmakeed.ui.UIPlugin;
import com.cthing.cmakeed.ui.prefs.Preferences;

/**
 * Manages all CMake document scanners.
 */
public class CMakeScannerMgr
{
    private ColorMgr colorMgr;
    
    /**
     * Constructor for the class.
     * 
     * @param colorMgr  Manages text editor colors.
     */
    public CMakeScannerMgr(final ColorMgr colorMgr)
    {
        this.colorMgr = colorMgr;
    }
    
    /**
     * Obtains a scanner corresponding to the specified partition content type.
     * 
     * @param contentType  Content type for which a scanner is desired.
     * @return The scanner corresponding to the specified partition content
     *      type or <code>null</code> if the content type is not recognized.
     */
    public ITokenScanner getScanner(final String contentType)
    {
        if (CMakePartitionScanner.isDefault(contentType)) {
            return getCMakeScanner();
        }
        if (CMakePartitionScanner.isComment(contentType)) {
            return getCommentScanner();
        }
        if (CMakePartitionScanner.isVariable(contentType)) {
            return getArgumentScanner();
        }
        if (CMakePartitionScanner.isString(contentType)) {
            return getStringScanner();
        }
        if (CMakePartitionScanner.isCommand(contentType)) {
            return getCommandScanner();
        }
        if (CMakePartitionScanner.isDepCommand(contentType)) {
            return getDepCommandScanner();
        }
        
        if (CMakePartitionScanner.isCMakeVariable(contentType)) {
        	return getCMakeVariableScanner();
        }
        if (CMakePartitionScanner.isProperty(contentType)) {
        	return getPropertyScanner();
        }
        if (CMakePartitionScanner.isReservedWord(contentType)) {
        	return getReservedWordScanner();
        }
        
        if (CMakePartitionScanner.isArgsOpen(contentType) ||
                CMakePartitionScanner.isArgsClose(contentType)) {
            return getCMakeScanner();
        }

        return null;
    }

    /**
     * Obtains the top level document scanner.
     * 
     * @return Top level document scanner
     */
    private CMakeScanner getCMakeScanner() 
    {
        final CMakeScanner scanner = new CMakeScanner();
        return scanner;
    }

    /**
     * Obtains the scanner for the comment partition.
     * 
     * @return Comment partition scanner.
     */
    private RuleBasedPartitionScanner getCommentScanner() 
    {
        return createRuleBasedScanner(Preferences.COMMENT);
    }

    /**
     * Obtains the scanner for the command argument partition.
     * 
     * @return Argument partition scanner.
     */
    private RuleBasedPartitionScanner getArgumentScanner() 
    {
        return createRuleBasedScanner(Preferences.VARIABLE);
    }

    /**
     * Obtains the scanner for the string partition.
     * 
     * @return String partition scanner.
     */
    private RuleBasedPartitionScanner getStringScanner() 
    {
        return createRuleBasedScanner(Preferences.STRING);
    }

    /**
     * Obtains the scanner for the command partition.
     * 
     * @return Command partition scanner.
     */
    private RuleBasedPartitionScanner getCommandScanner() 
    {
        return createRuleBasedScanner(Preferences.COMMAND);
    }

    /**
     * Obtains the scanner for the deprecated command partition.
     * 
     * @return Deprecated command partition scanner.
     */
    private RuleBasedPartitionScanner getDepCommandScanner() 
    {
        return createRuleBasedScanner(Preferences.DEP_COMMAND);
    }

    private RuleBasedPartitionScanner getCMakeVariableScanner()
    {
    	return createRuleBasedScanner(Preferences.CMAKE_VARIABLE);
    }
    
    private RuleBasedPartitionScanner getPropertyScanner()
    {
    	return createRuleBasedScanner(Preferences.CMAKE_PROPERTY);
    }
    
    private RuleBasedPartitionScanner getReservedWordScanner()
    {
    	return createRuleBasedScanner(Preferences.CMAKE_RESERVED_WORD);
    } 
    
    
    /**
     * Obtains a scanner with the specified text coloring attribute.
     * 
     * @param baseKey  Base preference key
     * @return Rule-based scanner with the specified text coloring attribute.
     */
    private RuleBasedPartitionScanner createRuleBasedScanner(final String baseKey)
    {
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
        
        final RGB color = PreferenceConverter.getColor(store, Preferences.getColorKey(baseKey));
        final int style = store.getInt(Preferences.getStyleKey(baseKey));

        final RuleBasedPartitionScanner scanner = new RuleBasedPartitionScanner();
        final TextAttribute attr = new TextAttribute(this.colorMgr.getColor(color), null, style);
        scanner.setDefaultReturnToken(new Token(attr));
        return scanner;
    }
}
