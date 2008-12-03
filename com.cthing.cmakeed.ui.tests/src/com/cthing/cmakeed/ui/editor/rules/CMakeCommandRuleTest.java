/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor.rules;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.cthing.cmakeed.ui.EditorTestCase;
import com.cthing.cmakeed.ui.editor.CMakePartitionScanner;

/**
 * Tests the ArgsCloseRule class.
 */
public class CMakeCommandRuleTest extends EditorTestCase
{
    private static final IToken SUCCESS = new Token(new Object());
    private CMakeCommandRule rule;
    
    /**
     * Default constructor for the test.
     */
    public CMakeCommandRuleTest()
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
        
        this.rule = new CMakeCommandRule(SUCCESS, false);
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
        final CMakePartitionScanner scanner = new CMakePartitionScanner();
        
        scanner.setRange(getDocument(), 42, 80);
        assertEquals(SUCCESS, this.rule.evaluate(scanner));
        
        scanner.setRange(getDocument(), 58, 80);
        assertEquals(Token.UNDEFINED, this.rule.evaluate(scanner));
    }
}
