/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor.rules;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

import com.cthing.cmakeed.ui.EditorTestCase;

/**
 * Tests the ArgsCloseRule class.
 */
public class ArgsCloseRuleTest extends EditorTestCase
{
    private static final IToken SUCCESS = new Token(new Object());
    private ArgsCloseRule rule;
    
    /**
     * Default constructor for the test.
     */
    public ArgsCloseRuleTest()
    {
    }
    
    /**
     * {@inheritDoc}
     * @see com.cthing.cmakeed.ui.EditorTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        
        this.rule = new ArgsCloseRule(SUCCESS);
    }

    /**
     * Tests the getSuccessToken method.
     */
    public void testGetSuccessToken()
    {
        assertEquals(SUCCESS, this.rule.getSuccessToken());
    }
    
    /**
     * Tests the evaluate method.
     */
    public void testEvaluate()
    {
        final RuleBasedScanner scanner = new RuleBasedScanner();
        
        scanner.setRange(getDocument(), 75, 80);
        assertEquals(SUCCESS, this.rule.evaluate(scanner));
        
        scanner.setRange(getDocument(), 70, 80);
        assertEquals(Token.UNDEFINED, this.rule.evaluate(scanner));
    }
}
