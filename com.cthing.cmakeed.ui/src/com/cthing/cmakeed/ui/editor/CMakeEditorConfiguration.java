/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
//import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import com.cthing.cmakeed.ui.CMakeEditorPlugin;

/**
 * Configures add-ons for the CMake document editor.
 */
public class CMakeEditorConfiguration extends TextSourceViewerConfiguration
{
    private CMakeScannerMgr scannerMgr;

    /**
     * Constructor for the class.
     *
     * @param colorMgr  Manager for the text editor colors.
     */
    public CMakeEditorConfiguration(final ColorMgr colorMgr)
    {
        super(CMakeEditorPlugin.getDefault().getPreferenceStore());
        this.scannerMgr = new CMakeScannerMgr(colorMgr);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer)
    {
        return CMakePartitionScanner.CMAKE_CONTENT_TYPES;
    }

	/**
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return CMakeEditorPlugin.CMAKE_PARTITIONING;
	}

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public IPresentationReconciler getPresentationReconciler(final ISourceViewer sourceViewer)
    {
    	PresentationReconciler reconciler= new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(IDocument.DEFAULT_CONTENT_TYPE));
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.COMMENT_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.COMMENT_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.COMMENT_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.COMMAND_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.COMMAND_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.COMMAND_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.DEPRECATED_COMMAND_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.DEPRECATED_COMMAND_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.DEPRECATED_COMMAND_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.VARREF_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.VARREF_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.VARREF_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.CMAKE_DEFINED_VARIABLE_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.CMAKE_DEFINED_VARIABLE_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.CMAKE_DEFINED_VARIABLE_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.PROPERTY_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.PROPERTY_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.PROPERTY_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.USER_VARIABLE_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.USER_VARIABLE_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.USER_VARIABLE_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.STRING_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.STRING_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.STRING_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));
        reconciler.setDamager(dr, CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE);
        return reconciler;
    }


    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public IContentAssistant getContentAssistant(final ISourceViewer sourceViewer)
    {
        ContentAssistant assistant = new ContentAssistant();
		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));


        assistant.setContentAssistProcessor(new CMakeContentAssistantProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
        assistant.setContentAssistProcessor(new CMakeContentAssistantProcessor(), CMakePartitionScanner.COMMAND_CONTENT_TYPE);
        assistant.setContentAssistProcessor(new CMakeContentAssistantProcessor(), CMakePartitionScanner.VARREF_CONTENT_TYPE);
        assistant.setContentAssistProcessor(new CMakeContentAssistantProcessor(), CMakePartitionScanner.CMAKE_DEFINED_VARIABLE_CONTENT_TYPE);
        assistant.setContentAssistProcessor(new CMakeContentAssistantProcessor(), CMakePartitionScanner.PROPERTY_CONTENT_TYPE);
        assistant.setContentAssistProcessor(new CMakeContentAssistantProcessor(), CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE);
        assistant.setContentAssistProcessor(new CMakeContentAssistantProcessor(), CMakePartitionScanner.USER_VARIABLE_CONTENT_TYPE);

        assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);
		assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);

        return assistant;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTextHover(org.eclipse.jface.text.source.ISourceViewer, java.lang.String, int)
     */
    @Override
    public ITextHover getTextHover(final ISourceViewer sourceViewer,
                                   final String contentType)
    {
      //  if (CMakePartitionScanner.isAnyCommand(contentType)) {
            return new CMakeEditorTextHover();
     //   }
    //    return null;
    }

}
