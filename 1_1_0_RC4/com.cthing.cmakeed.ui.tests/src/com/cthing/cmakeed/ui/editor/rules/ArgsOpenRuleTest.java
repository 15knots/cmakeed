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
public class ArgsOpenRuleTest extends EditorTestCase
{
    private static final IToken SUCCESS = new Token(new Object());
    private ArgsOpenRule rule;
    
    /**
     * Default constructor for the test.
     */
    public ArgsOpenRuleTest()
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
        
        this.rule = new ArgsOpenRule(SUCCESS);
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
        
        scanner.setRange(getDocument(), 57, 80);
        assertEquals(SUCCESS, this.rule.evaluate(scanner));
        
        scanner.setRange(getDocument(), 60, 80);
        assertEquals(Token.UNDEFINED, this.rule.evaluate(scanner));
    }
}
