/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

import com.cthing.cmakeed.ui.EditorTestCase;

/**
 * Tests the CMake document provider class.
 */
public class CMakeDocumentProviderTest extends EditorTestCase
{
    /**
     * Test wrapper class.
     */
    private class DocumentProvider extends CMakeDocumentProvider
    {
        /**
         * Default constructor for the class.
         */
        public DocumentProvider()
        {
        }
        
        /**
         * {@inheritDoc}
         * @see com.cthing.cmakeed.ui.editor.CMakeDocumentProvider#createDocument(java.lang.Object)
         */
        @Override
        protected IDocument createDocument(final Object element) throws CoreException
        {
            return super.createDocument(element);
        }
        
        /**
         * Allows the test class to create a document using the provider.
         * 
         * @param input  Editor input
         * @return Created document
         * @throws CoreException If there is a problem creating the document.
         */
        public IDocument makeDocument(final IEditorInput input) throws CoreException
        {
            return createDocument(input);
        }
    }

    /**
     * Default constructor for the class.
     */
    public CMakeDocumentProviderTest()
    {
    }
    
    /**
     * Tests the createDocument method.
     */
    public void testCreateDocument()
    {
        final FileEditorInput input = new FileEditorInput(getFile());
        final DocumentProvider provider = new DocumentProvider();
        
        try {
            final IDocument doc = provider.makeDocument(input);
            assertNotNull(doc);
        }
        catch (final CoreException e) {
            fail(e.getMessage());
        }
    }
}
