/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor.rules;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

import com.cthing.cmakeed.core.uservariables.CMakeUserVariable;
import com.cthing.cmakeed.core.uservariables.CMakeUserVariables;
import com.cthing.cmakeed.ui.editor.CMakeNameDetector;
import com.cthing.cmakeed.ui.editor.CMakePartitionScanner;
import com.cthing.cmakeed.ui.editor.EditorUtils;

/**
 * Identifies a CMake command.
 */
public class CMakeUserVariableRule implements IRule, IPredicateRule
{
    private IToken userVariableToken;
 //   private boolean findDeprecated;
    private IWordDetector detector = new CMakeNameDetector();
    private StringBuilder buffer = new StringBuilder();

    /**
     * Maps the an IDocument to a list of user defined variables
     */
    public static Map<IDocument, CMakeUserVariables> userVariableMap;

    /**
     * Constructor for the class.
     *
     * @param commandToken  Token to return if a CMake command is found
     */
    public CMakeUserVariableRule(final IToken commandToken)
    {
        this.userVariableToken = commandToken;
        if (CMakeUserVariableRule.userVariableMap == null)
        {
        	CMakeUserVariableRule.userVariableMap = new LinkedHashMap<IDocument, CMakeUserVariables>();
        }
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
     */
    public IToken evaluate(final ICharacterScanner scanner) {
        // Make sure we are inside a command first and at the start of a word
        if (scanner instanceof CMakePartitionScanner) {
            final CMakePartitionScanner cscan = (CMakePartitionScanner) scanner;
            final IDocument doc = cscan.getDocument();

            CMakeUserVariables userVariables = CMakeUserVariableRule.userVariableMap.get(doc);
            if (null == userVariables) {
                userVariables = new CMakeUserVariables();
                userVariableMap.put(doc, userVariables);
            }

            final int offset = cscan.getTokenOffset();
            if (EditorUtils.inArguments(doc, offset)) {
                if (EditorUtils.startOfWord(doc, offset)) {
                    this.buffer.setLength(0);
                    // Read the word into the buffer
                    for (int ch = scanner.read();
                        ch != ICharacterScanner.EOF && this.detector.isWordPart((char) ch);
                        ch = scanner.read())
                    {
                        this.buffer.append((char) ch);
                    }
                    scanner.unread();
                    final CMakeUserVariable var = userVariables.getUserVariable(this.buffer.toString());
                    if (var != null) {
                        return this.userVariableToken;
                    } else {
                        String uvar = this.buffer.toString();
                        uvar = uvar.trim(); // trim off white space
                        if (uvar.length() > 0 && uvar.contains(".") == false && uvar.contains("/") == false
                                && uvar.startsWith("/") == false && uvar.startsWith("-") == false
                                && EditorUtils.firstArgument(doc, offset) == true) {
                            userVariables.addUserVariable(uvar);
                            return this.userVariableToken;
                        }

                    }
                    EditorUtils.unread(scanner, this.buffer);
                }
            }

        }
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
        return this.userVariableToken;
    }
}
