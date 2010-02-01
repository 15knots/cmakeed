/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor.rules;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

import com.cthing.cmakeed.core.commands.CMakeCommand;
import com.cthing.cmakeed.core.commands.CMakeCommands;
import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.editor.CMakeNameDetector;
import com.cthing.cmakeed.ui.editor.CMakePartitionScanner;
import com.cthing.cmakeed.ui.editor.EditorUtils;

/**
 * Identifies a CMake command.
 */
public class CMakeCommandRule implements IRule, IPredicateRule
{
    private IToken commandToken;
    private boolean findDeprecated;
    private IWordDetector detector = new CMakeNameDetector();
    private StringBuilder buffer = new StringBuilder();
    private int lastOffsetUsedStart = 0;
    private int lastOffsetUsedEnd = 1;
  
    /**
     * Constructor for the class.
     * 
     * @param commandToken  Token to return if a CMake command is found
     * @param findDeprecated  <code>true</code> if deprecated commands are
     *      to be found. Otherwise, only non-deprecated commands are found.
     */
    public CMakeCommandRule(final IToken commandToken, final boolean findDeprecated)
    {
        this.commandToken = commandToken;
        this.findDeprecated = findDeprecated;
    }
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
     */
    public IToken evaluate(final ICharacterScanner scanner)
    {   
//        if (this.findDeprecated == true)
//        {
//            return Token.UNDEFINED;
//        }
     //   System.out.println("-------------------------------------------------");
        
        // Clear the buffer
    //    for (int index = 0; index < buffer.length(); index++)
    //    {
    //        this.buffer.setCharAt(index, (char)0);
    //    }
        
        final CMakePartitionScanner cscan = (CMakePartitionScanner)scanner;
        final IDocument doc = cscan.getDocument();
        int offset = cscan.getOffset();
       // System.out.println("  Starting Offset: " + offset);
        if (offset > this.lastOffsetUsedStart 
                && offset < this.lastOffsetUsedEnd  )
        {
            return Token.UNDEFINED;
        }
        String prefix = "";
        try {
            char c = doc.getChar(offset);
            if ( true == detector.isWordPart(c) )
            {
                prefix = getPrefix(doc, offset);
            }
        } catch (BadLocationException e) {
           // e.printStackTrace();
        }
        
        this.buffer.setLength(0);        
        for (int ch = scanner.read();
                ch != ICharacterScanner.EOF && this.detector.isWordPart((char)ch);
                ch = scanner.read()) {
            this.buffer.append((char)ch);
        }
        scanner.unread();
        
//        System.out.println("Buffer: '" + buffer + "'");
//        System.out.println("Prefix: '" + prefix + "'");
        
        final CMakeCommand cmd = CMakeCommands.getCommand(prefix + this.buffer.toString());
        if ((cmd != null) && (cmd.isDeprecated() == this.findDeprecated)) 
        {
            if (scanner instanceof CMakePartitionScanner) 
            {
                offset = cscan.getTokenOffset();
                if (!EditorUtils.inArguments(doc, offset)) 
                {
                    this.lastOffsetUsedEnd = -1;
                    this.lastOffsetUsedStart = -2;
                    for (int index = 0; index < buffer.length(); index++) {
                        this.buffer.setCharAt(index, (char) 0);
                    }
                    return this.commandToken;
                }
            }
        }
       
        this.lastOffsetUsedEnd = cscan.getOffset();
        EditorUtils.unread(scanner, this.buffer);
        this.lastOffsetUsedStart = cscan.getOffset();
        return Token.UNDEFINED;
    }

    /**
     * Determines the portion of the word that has already been entered.
     * 
     * @param doc  Document being edited
     * @param offset  Offset into the document where content assist is desired.
     * @return Portion of the word that has already been entered.
     */
    private String getPrefix(final IDocument doc, final int offset)
    {
        try {
            for (int n = offset - 1; n >= 0; n--) {
                final char c = doc.getChar(n);
                if (!this.detector.isWordPart(c)) {
                    return doc.get(n + 1, offset - n - 1);
                }
                if (n == 0)
                {
                    return doc.get(n, offset - n);
                }
            }
        }
        catch (final BadLocationException e) {
            CMakeEditorPlugin.logError(this, e);
        }
        
        return "";      //$NON-NLS-1$
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
        return this.commandToken;
    }
}
