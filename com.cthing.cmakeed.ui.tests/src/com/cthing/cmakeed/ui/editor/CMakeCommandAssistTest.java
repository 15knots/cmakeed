/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.cthing.cmakeed.ui.EditorTestCase;

/**
 * Tests the CMakeCommandAssist class.
 */
public class CMakeCommandAssistTest extends EditorTestCase
{
    private CMakeContentAssistantProcessor assist;
    
    /**
     * Default constructor for the class.
     */
    public CMakeCommandAssistTest()
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
        
        this.assist = new CMakeContentAssistantProcessor();
    }
    
    /**
     * Tests the computeCompletionProposals method.
     */
    public void testComputeCompletionProposals()
    {
        ICompletionProposal[] proposals =
            this.assist.computeCompletionProposals(getViewer(), 50);
        assertNotNull(proposals);
        assertTrue(proposals.length > 0);

        proposals = this.assist.computeCompletionProposals(getViewer(), 62);
        assertNull(proposals);
    }
    
    /**
     * Tests the computeContextInformation method.
     */
    public void testComputeContextInformation()
    {
        IContextInformation[] info =
            this.assist.computeContextInformation(getViewer(), 62);
        assertNotNull(info);
        assertTrue(info.length > 0);
        
        info = this.assist.computeContextInformation(getViewer(), 62);
        assertNotNull(info);
        assertTrue(info.length > 0);

        info = this.assist.computeContextInformation(getViewer(), 77);
        assertNull(info);
    }
    
    /**
     * Tests the getContextInformationValidator method.
     */
    public void testGetContextInformationValidator()
    {
        final IContextInformationValidator validator =
            this.assist.getContextInformationValidator();
        assertNotNull(validator);
    }
    
    /**
     * Tests the getCompletionProposalAutoActivationCharacters method.
     */
    public void testGetCompletionProposalAutoActivationCharacters()
    {
        final char[] chars =
            this.assist.getCompletionProposalAutoActivationCharacters();
        assertNotNull(chars);
        assertTrue(chars.length > 0);
    }
    
    /**
     * Tests the getContextInformationAutoActivationCharacters method.
     */
    public void testGetContextInformationAutoActivationCharacters()
    {
        final char[] chars =
            this.assist.getContextInformationAutoActivationCharacters();
        assertNotNull(chars);
        assertTrue(chars.length > 0);
    }
    
    /**
     * Tests the getHoverRegion method.
     */
    public void testGetHoverRegion()
    {
        IRegion region = this.assist.getHoverRegion(getViewer(), 50);
        assertNotNull(region);
        assertEquals(42, region.getOffset());
        assertEquals(14, region.getLength());

        region = this.assist.getHoverRegion(getViewer(), -1);
        assertNull(region);

        region = this.assist.getHoverRegion(getViewer(),
                                            getDocument().getLength() + 1);
        assertNull(region);
    }
    
    /**
     * Tests the getHoverInfo method.
     */
    public void testGetHoverInfo()
    {
        String info = this.assist.getHoverInfo(getViewer(),
                                this.assist.getHoverRegion(getViewer(), 50));
        assertNotNull(info);
        assertTrue(info.length() > 0);
        
        info = this.assist.getHoverInfo(getViewer(), null);
        assertNull(info);
    }
}
