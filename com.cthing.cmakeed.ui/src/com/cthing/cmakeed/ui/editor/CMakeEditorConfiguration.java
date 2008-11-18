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
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import com.cthing.cmakeed.ui.UIPlugin;
import com.cthing.cmakeed.ui.prefs.Preferences;

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
        this.scannerMgr = new CMakeScannerMgr(colorMgr);
    }
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer)
    {
        return CMakePartitionScanner.CONTENT_TYPES;
    }

    /**
     * {@inheritDoc}
     * 
     * This implementation was copied from
     * org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration.
     * 
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getIndentPrefixes(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    @Override
    public String[] getIndentPrefixes(final ISourceViewer sourceViewer,
                                      final String contentType)
    {
        final List<String> list = new ArrayList<String>();
        
        // The prefix[0] is either '\t' or ' ' x tabWidth, depending on
        // useSpaces.

        final int tabWidth = getTabWidth(sourceViewer);
        
        final IPreferenceStore prefStore = UIPlugin.getDefault().getPreferenceStore();
        final boolean useSpaces = prefStore.getBoolean(Preferences.SPACES_FOR_TABS);

        for (int i = 0; i <= tabWidth; i++) {
            final StringBuilder prefix = new StringBuilder();
            if (useSpaces) {
                for (int j = 0; j + i < tabWidth; j++) {
                    prefix.append(' ');
                }
                if (i != 0) {
                    prefix.append('\t');
                }
            } else {
                for (int j = 0; j < i; j++) {
                    prefix.append(' ');
                }
                if (i != tabWidth) {
                    prefix.append('\t');
                }
            }
            list.add(prefix.toString());
        }
        
        list.add("");   //$NON-NLS-1$ 
        return list.toArray(new String[list.size()]);
    }
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public IContentAssistant getContentAssistant(final ISourceViewer sourceViewer)
    {
        final ContentAssistant ca = new ContentAssistant();
        final IContentAssistProcessor pr = new CMakeContentAssistantProcessor();
//        final IContentAssistProcessor propProc = new CMakePropertyAssist();
//        final IContentAssistProcessor varProc = new CMakeVariableAssist();
//        final IContentAssistProcessor resProc = new CMakeReservedWordAssist();
        
        ca.setContentAssistProcessor(pr, IDocument.DEFAULT_CONTENT_TYPE);
//        ca.setContentAssistProcessor(pr, CMakePartitionScanner.COMMAND_CONTENT_TYPE);
//        ca.setContentAssistProcessor(propProc, CMakePartitionScanner.PROPERTY_CONTENT_TYPE);
//        ca.setContentAssistProcessor(varProc, CMakePartitionScanner.VARIABLE_CONTENT_TYPE);
//        ca.setContentAssistProcessor(resProc, CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE);
        ca.setInformationControlCreator(getInformationControlCreator(sourceViewer));
        ca.enableAutoActivation(true);
        
        return ca;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTextHover(org.eclipse.jface.text.source.ISourceViewer, java.lang.String, int)
     */
    @Override
    public ITextHover getTextHover(final ISourceViewer sourceViewer,
                                   final String contentType)
    {
        if (CMakePartitionScanner.isAnyCommand(contentType)) {
            return new CMakeContentAssistantProcessor();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public IPresentationReconciler getPresentationReconciler(final ISourceViewer sourceViewer) 
    {
        final PresentationReconciler reconciler = new PresentationReconciler();
        
        DefaultDamagerRepairer dr =
            new DefaultDamagerRepairer(this.scannerMgr.getScanner(IDocument.DEFAULT_CONTENT_TYPE));
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.COMMENT_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.COMMENT_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.COMMENT_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.VARREF_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.VARREF_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.VARREF_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.STRING_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.STRING_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.STRING_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.COMMAND_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.COMMAND_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.COMMAND_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.DEP_COMMAND_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.DEP_COMMAND_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.DEP_COMMAND_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.ARGS_OPEN_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.ARGS_CLOSE_CONTENT_TYPE);


        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.VARIABLE_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.VARIABLE_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.VARIABLE_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.PROPERTY_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.PROPERTY_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.PROPERTY_CONTENT_TYPE);
        
        dr = new DefaultDamagerRepairer(this.scannerMgr.getScanner(CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE));    
        reconciler.setDamager(dr, CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE);
        reconciler.setRepairer(dr, CMakePartitionScanner.RESERVED_WORD_CONTENT_TYPE);
        
        return reconciler;
    }
}
