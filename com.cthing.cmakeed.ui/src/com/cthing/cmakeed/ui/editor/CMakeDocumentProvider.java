/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * Provides CMake documents (e.g. CMakeLists.txt).
 */
public class CMakeDocumentProvider extends FileDocumentProvider
{
    /**
     * Default constructor for the class.
     */
    public CMakeDocumentProvider()
    {
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createDocument(java.lang.Object)
     */
    @Override
    protected IDocument createDocument(final Object element) throws CoreException
    {
        final IDocument document = super.createDocument(element);
        if (document != null) {
            final IDocumentPartitioner partitioner =
                new FastPartitioner(new CMakePartitionScanner(),
                                    CMakePartitionScanner.CONTENT_TYPES);
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);
        }
        
        return document;
    }
}
