/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;

import com.cthing.cmakeed.ui.EditorTestCase;

/**
 * Tests the CMakeEditorCOnfiguration class.
 */
public class CMakeEditorConfigurationTest extends EditorTestCase
{
    private CMakeEditorConfiguration conf;

    /**
     * Default constructor for the class.
     */
    public CMakeEditorConfigurationTest()
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

        this.conf = new CMakeEditorConfiguration(new ColorMgr());
    }

    /**
     * Tests the getConfiguredContentTypes method.
     */
    public void testGetConfiguredContentTypes()
    {
        final String[] contentTypes =
            this.conf.getConfiguredContentTypes(getViewer());
        assertNotNull(contentTypes);
        assertEquals(CMakePartitionScanner.CMAKE_CONTENT_TYPES, contentTypes);
    }
    
    /**
     * Tests the getIndentPrefixes method.
     */
    public void testGetIndentPrefixes()
    {
        final String[] prefixes = this.conf.getIndentPrefixes(getViewer(),
                CMakePartitionScanner.COMMAND_CONTENT_TYPE);
        assertNotNull(prefixes);
        assertTrue(prefixes.length > 0);
    }
    
    /**
     * Tests the getContentAssist method.
     */
    public void testGetContentAssist()
    {
        final IContentAssistant ca = this.conf.getContentAssistant(getViewer());
        assertNotNull(ca);
    }
    
    /**
     * Tests the getTextHover method.
     */
    public void testGetTextHover()
    {
        ITextHover hover = this.conf.getTextHover(getViewer(),
                CMakePartitionScanner.COMMAND_CONTENT_TYPE);
        assertNotNull(hover);
        
        hover = this.conf.getTextHover(getViewer(),
                CMakePartitionScanner.DEPRECATED_COMMAND_CONTENT_TYPE);
        assertNotNull(hover);
        
        hover = this.conf.getTextHover(getViewer(),
                CMakePartitionScanner.COMMENT_CONTENT_TYPE);
        assertNull(hover);
    }
    
    /**
     * Tests the getPresentationReconciler method.
     */
    public void testGetPresentationReconciler()
    {
        final IPresentationReconciler pr =
            this.conf.getPresentationReconciler(getViewer());
        assertNotNull(pr);
    }
}
