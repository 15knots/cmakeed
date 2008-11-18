/* *****************************************************************************
 * Copyright 2008 BlueQuartz Software, Michael Jackson
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor.rules;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

import com.cthing.cmakeed.core.properties.CMakeProperty;
import com.cthing.cmakeed.core.properties.CMakeProperties;
import com.cthing.cmakeed.ui.editor.CMakeNameDetector;
import com.cthing.cmakeed.ui.editor.CMakePartitionScanner;
import com.cthing.cmakeed.ui.editor.EditorUtils;

/**
 * Identifies a CMake command.
 */
public class CMakePropertyRule implements IRule, IPredicateRule
{
    private IToken propertyToken;
    private boolean findDeprecated;
    private IWordDetector detector = new CMakeNameDetector();
    private StringBuilder buffer = new StringBuilder();
    
    /**
     * Constructor for the class.
     * 
     * @param commandToken  Token to return if a CMake command is found
     * @param findDeprecated  <code>true</code> if deprecated commands are
     *      to be found. Otherwise, only non-deprecated commands are found.
     */
    public CMakePropertyRule(final IToken commandToken, final boolean findDeprecated)
    {
        this.propertyToken = commandToken;
        this.findDeprecated = findDeprecated;
    }
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
     */
    public IToken evaluate(final ICharacterScanner scanner)
    {
        this.buffer.setLength(0);
        
        for (int ch = scanner.read();
                ch != ICharacterScanner.EOF && this.detector.isWordPart((char)ch);
                ch = scanner.read()) {
            this.buffer.append((char)ch);
        }
        scanner.unread();
        
        final CMakeProperty cmd = CMakeProperties.getCommand(this.buffer.toString());
        if ((cmd != null) && (cmd.isDeprecated() == this.findDeprecated)) {
            if (scanner instanceof CMakePartitionScanner) {
                final CMakePartitionScanner cscan = (CMakePartitionScanner)scanner;
                final IDocument doc = cscan.getDocument();
                final int offset = cscan.getTokenOffset();
                if (EditorUtils.inArguments(doc, offset) && EditorUtils.startOfWord(doc, offset))
                {
                    return this.propertyToken;
                }
            }
        }

        EditorUtils.unread(scanner, this.buffer);
        
        return Token.UNDEFINED;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
     */
    public IToken evaluate(final ICharacterScanner scanner, final boolean resume)
    {
        return evaluate(scanner);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
     */
    public IToken getSuccessToken()
    {
        return this.propertyToken;
    }
}
