/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * Copyright 2008 BlueQuartz Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

import com.cthing.cmakeed.ui.Messages;
import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.prefs.Preferences;
import com.cthing.cmakeed.ui.editor.CMakeEditorConfiguration;

/**
 * Editor for CMake files.
 */
public class CMakeEditor extends AbstractDecoratedTextEditor
                         implements IPropertyChangeListener
{
    private StyledText text;
    private ColorMgr colorMgr;

    /**
     *
     */
    private VerifyListener tabReplacer = new VerifyListener() {
        public void verifyText(final VerifyEvent event)
        {
            if (event.text.equals("\t")) {          //$NON-NLS-1$
                final StringBuilder tab = new StringBuilder();
                for (int idx = 0; idx < CMakeEditor.this.text.getTabs(); idx++) {
                    tab.append(' ');
                }
                event.text = tab.toString();
            }
        }
    };

    /**
     * Default constructor for the class.
     */
    public CMakeEditor() {
		super();
		IDocumentProvider provider = new TextFileDocumentProvider();
		provider = new ForwardingDocumentProvider(CMakeEditorPlugin.CMAKE_PARTITIONING,
				new CMakeDocumentSetupParticipant(), provider);
		setDocumentProvider(provider);
		final IPreferenceStore store = CMakeEditorPlugin.getDefault().getPreferenceStore();

		store.addPropertyChangeListener(this);
	}

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(final Composite parent)
    {
        super.createPartControl(parent);

        this.text = getSourceViewer().getTextWidget();

        updateTabReplacer();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#createActions()
     */
    @Override
    protected void createActions()
    {
        super.createActions();

        final Action action =
            new ContentAssistAction(Messages.getResourceBundle(),
                                    "ContentProposal.", this);      //$NON-NLS-1$
        final String id = ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS;
        action.setActionDefinitionId(id);
        setAction("ContentAssistProposal", action);                 //$NON-NLS-1$
        markAsStateDependentAction("ContentAssistProposal", true);  //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(final java.lang.Class key)
    {
        if (key.equals(ISourceViewer.class)) {
            return getSourceViewer();
        }

        if (key.equals(StyledText.class)) {
            return this.text;
        }

        return super.getAdapter(key);
    }

	/* (non-Javadoc)
	 * Method declared on AbstractTextEditor
	 */
	protected void initializeEditor() {
		super.initializeEditor();
		if (null == this.colorMgr) {
			this.colorMgr = new ColorMgr();
		}
		setSourceViewerConfiguration(new CMakeEditorConfiguration(this.colorMgr));
	}

	/** The <code>JavaEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs sets the
	 * input of the outline page after AbstractTextEditor has set input.
	 *
	 * @param input the editor input
	 * @throws CoreException in case the input can not be set
	 */
	public void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		CMakeEditorPlugin.getDefault().getCMakePartitionScanner().removeUserVariableRule();
	}


	/**
	 * Saves the document to the underlying filebuffer.
	 * @param prog The Progress monitor to use
	 */
	public void doSave(IProgressMonitor prog)
	{
		super.doSave(prog);
		CMakeEditorPlugin.getDefault().getCMakePartitionScanner().setDefaultScannerRules();
		if(false){
		  /* WW: disabled, since this wipes out the undo history (#4).
		   * Tested it manually, neither the NPE when saving a file that was opened with
		   * the CMakeEd plugin but did not have the .cmake extension hanppened nor any
		   * issue where opening an arbitrary cmake file with the CMakeEd Plugin
		   * did not produce the correct syntax highlighting or completions.
		   */
		IDocument document = this.getSourceViewer().getDocument();
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			IDocumentPartitioner partitioner = extension3.getDocumentPartitioner(CMakeEditorPlugin.CMAKE_PARTITIONING);
			if (partitioner != null)
			{
				partitioner.disconnect();
				partitioner.connect(document);
				// Reinit the syntax coloring
				//TODO: Get the current Cursor Position and restore that also.
				final SourceViewer sourceViewer = (SourceViewer)getSourceViewer();
	            int line = sourceViewer.getTopIndex();
	            ITextSelection sel = (ITextSelection)sourceViewer.getSelection();
				sourceViewer.unconfigure();
	            final CMakeEditorConfiguration config = new CMakeEditorConfiguration(this.colorMgr);
	            sourceViewer.configure(config);
	            sourceViewer.refresh();
	            sourceViewer.setTopIndex(line);
	            sourceViewer.setSelectedRange(sel.getOffset(), sel.getLength()	);
			}

		}
}
		CMakeEditorPlugin.getDefault().getCMakePartitionScanner().removeUserVariableRule();
	}


    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.editors.text.TextEditor#dispose()
     */
    @Override
    public void dispose()
    {
        final IPreferenceStore store = CMakeEditorPlugin.getDefault().getPreferenceStore();
        store.removePropertyChangeListener(this);

        this.colorMgr.dispose();

        super.dispose();
    }

    /**
     * Adds or removes the tab replacer based on the preference setting.
     */
    private void updateTabReplacer()
    {
        final IPreferenceStore prefStore = CMakeEditorPlugin.getDefault().getPreferenceStore();
        if (prefStore.getBoolean(Preferences.SPACES_FOR_TABS)) {
            this.text.addVerifyListener(this.tabReplacer);
        }
        else {
            this.text.removeVerifyListener(this.tabReplacer);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent event)
    {
        if (event == null || event.getProperty().equals(Preferences.SPACES_FOR_TABS)) {
            updateTabReplacer();
        }

        if (event == null || Preferences.isTextPreference(event.getProperty())) {
            final SourceViewer sourceViewer = (SourceViewer)getSourceViewer();
            int line = sourceViewer.getTopIndex();
			sourceViewer.unconfigure();
            final CMakeEditorConfiguration config = new CMakeEditorConfiguration(this.colorMgr);
            sourceViewer.configure(config);
            sourceViewer.refresh();
            sourceViewer.setTopIndex(line);
        }
    }

    /**
     * Get the spefile source viewer, this method is useful for test cases.
     *
     * @return
     *      the specfile source viewer
     */
    public SourceViewer getCMakeEditorSourceViewer() {
        return (SourceViewer) getSourceViewer();
    }
}
