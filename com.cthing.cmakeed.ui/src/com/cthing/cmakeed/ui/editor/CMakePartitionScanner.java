/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import com.cthing.cmakeed.ui.editor.rules.ArgsCloseRule;
import com.cthing.cmakeed.ui.editor.rules.ArgsOpenRule;
import com.cthing.cmakeed.ui.editor.rules.CMakeCommandRule;
import com.cthing.cmakeed.ui.editor.rules.CMakePropertyRule;
import com.cthing.cmakeed.ui.editor.rules.CMakeReservedWordRule;
import com.cthing.cmakeed.ui.editor.rules.CMakeUserVariableRule;
import com.cthing.cmakeed.ui.editor.rules.CMakeVariableRule;

/**
 * Identifies partitions within a CMake file.
 */
public class CMakePartitionScanner extends RuleBasedPartitionScanner
{
    /** Comment partition. */
    public static final String COMMENT_CONTENT_TYPE = "__comment";  //$NON-NLS-1$
    /** Variable reference partition. */
    public static final String VARREF_CONTENT_TYPE = "__varref";    //$NON-NLS-1$
    /** String partition. */
    public static final String STRING_CONTENT_TYPE = "__string";    //$NON-NLS-1$
    /** Command partition. */
    public static final String COMMAND_CONTENT_TYPE = "__command";    //$NON-NLS-1$
    /** Deprecated command partition. */
    public static final String DEP_COMMAND_CONTENT_TYPE = "__dep_command";    //$NON-NLS-1$
    /** Command arguments open. */
    public static final String ARGS_OPEN_CONTENT_TYPE = "__args_open";      //$NON-NLS-1$
    /** Command arguments close. */
    public static final String ARGS_CLOSE_CONTENT_TYPE = "__args_close";      //$NON-NLS-1$
    /** Variable partition. */
    public static final String VARIABLE_CONTENT_TYPE = "__variable";    //$NON-NLS-1$
    /** Property partition. */
    public static final String PROPERTY_CONTENT_TYPE = "__property";    //$NON-NLS-1$
    /** Reserved Word partition. */
    public static final String RESERVED_WORD_CONTENT_TYPE = "__reservedword";    //$NON-NLS-1$
    
    public static final String USER_VARIABLE_CONTENT_TYPE = "__uservariable";  //$NON-NLS-1$

    /** Array of all partition types. */
    public static final String[] CMAKE_CONTENT_TYPES = new String[] {
        IDocument.DEFAULT_CONTENT_TYPE, 
        COMMENT_CONTENT_TYPE,
        VARREF_CONTENT_TYPE,
        STRING_CONTENT_TYPE,
        COMMAND_CONTENT_TYPE,
        DEP_COMMAND_CONTENT_TYPE,
        ARGS_OPEN_CONTENT_TYPE,
        ARGS_CLOSE_CONTENT_TYPE,
        VARIABLE_CONTENT_TYPE,
        PROPERTY_CONTENT_TYPE,
        RESERVED_WORD_CONTENT_TYPE,
        USER_VARIABLE_CONTENT_TYPE,
    };

    /**
     * Default constructor for the class.
     */
    public CMakePartitionScanner()
    {
        final IPredicateRule[] preds = new IPredicateRule[] {
                new EndOfLineRule("#", new Token(COMMENT_CONTENT_TYPE)),    //$NON-NLS-1$
                new SingleLineRule("${", "}", new Token(VARREF_CONTENT_TYPE)),   //$NON-NLS-1$ //$NON-NLS-2$
                new SingleLineRule("@", "@", new Token(VARREF_CONTENT_TYPE)),   //$NON-NLS-1$ //$NON-NLS-2$
                new SingleLineRule("\"", "\"", new Token(STRING_CONTENT_TYPE)),   //$NON-NLS-1$ //$NON-NLS-2$
                new CMakeCommandRule(new Token(COMMAND_CONTENT_TYPE), false),
                new CMakeCommandRule(new Token(DEP_COMMAND_CONTENT_TYPE), true),
                new ArgsOpenRule(new Token(ARGS_OPEN_CONTENT_TYPE)),                
                new CMakePropertyRule(new Token(PROPERTY_CONTENT_TYPE), false),
                new CMakeVariableRule(new Token(VARIABLE_CONTENT_TYPE), false),
                new CMakeReservedWordRule(new Token(RESERVED_WORD_CONTENT_TYPE), false),
                new CMakeUserVariableRule(new Token(USER_VARIABLE_CONTENT_TYPE)),
                new ArgsCloseRule(new Token(ARGS_CLOSE_CONTENT_TYPE)),
            };

        setPredicateRules(preds);
    }
    
    /**
     * Obtains the document being scanned.
     * 
     * @return Document being scanned.
     */
    public IDocument getDocument()
    {
        return this.fDocument;
    }
    
    /**
     * Indicates whether the specified content type represents a user variable
     * partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      comment partition.
     */
    public static boolean isUserVariable(final String contentType)
    {
        return USER_VARIABLE_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents the default
     * partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents the
     *      default partition.
     */
    public static boolean isDefault(final String contentType)
    {
        return IDocument.DEFAULT_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a comment
     * partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      comment partition.
     */
    public static boolean isComment(final String contentType)
    {
        return COMMENT_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a variable
     * reference partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      variable reference partition.
     */
    public static boolean isVariable(final String contentType)
    {
        return VARREF_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a string
     * partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      string partition.
     */
    public static boolean isString(final String contentType)
    {
        return STRING_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a command
     * or deprecated command partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      any type of command partition.
     */
    public static boolean isAnyCommand(final String contentType)
    {
        return isCommand(contentType) || isDepCommand(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a command
     * partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      command partition.
     */
    public static boolean isCommand(final String contentType)
    {
        return COMMAND_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a property
     * partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      property partition.
     */
    public static boolean isProperty(final String contentType)
    {
        return PROPERTY_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a cmake variable
     * partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      cmake variable partition.
     */
    public static boolean isCMakeVariable(final String contentType)
    {
        return VARIABLE_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a cmake reserved
     * word partition
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      cmake variable partition.
     */
    public static boolean isReservedWord(final String contentType)
    {
        return RESERVED_WORD_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents a deprecated
     * command partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents a
     *      deprecated command partition.
     */
    public static boolean isDepCommand(final String contentType)
    {
        return DEP_COMMAND_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents an arguments
     * open partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents an
     *      arguments open partition.
     */
    public static boolean isArgsOpen(final String contentType)
    {
        return ARGS_OPEN_CONTENT_TYPE.equals(contentType);
    }
    
    /**
     * Indicates whether the specified content type represents an arguments
     * close partition.
     * 
     * @param contentType  Partition content type.
     * @return <code>true</code> if the specified content type represents an
     *      arguments close partition.
     */
    public static boolean isArgsClose(final String contentType)
    {
        return ARGS_CLOSE_CONTENT_TYPE.equals(contentType);
    }
}
