/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Identifies the opening parenthesis of a CMake command.
 */
public class ArgsOpenRule implements IPredicateRule, IRule
{
    private IToken argsOpenToken;
    
    
    /**
     * Constructor for the class.
     * 
     * @param argsOpenToken  Token to send back when a command's opening
     *      parenthesis is detected.
     */
    public ArgsOpenRule(final IToken argsOpenToken)
    {
        this.argsOpenToken = argsOpenToken;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
     */
    public IToken getSuccessToken()
    {
        return this.argsOpenToken;
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
     * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
     */
    public IToken evaluate(final ICharacterScanner scanner)
    {
        final int ch = scanner.read();
        if (ch == '(') {
            return this.argsOpenToken;
        }
        scanner.unread();
        return Token.UNDEFINED;
    }

}
