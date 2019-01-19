/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * Copyright 2008 BlueQuartz Software
 * Copyright 2018 Martin Weber
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.Messages;
import com.cthing.cmakeed.ui.prefs.Preferences;

/**
 * Editor for CMake files.
 */
public class CMakeEditor extends TextEditor
                         implements IPropertyChangeListener
{
  /** the brackets to match */
  private static final char[] matchChars = { '(', ')', '[', ']', '<', '>' };

    private StyledText text;
    private ColorMgr colorMgr;

    /**
     * Default constructor for the class.
     */
    public CMakeEditor() {
		super();
		final IDocumentProvider provider =
		    new ForwardingDocumentProvider(CMakeEditorPlugin.CMAKE_PARTITIONING,
			new CMakeDocumentSetupParticipant(), new TextFileDocumentProvider());
		setDocumentProvider(provider);
	}

    @Override
    protected String[] collectContextMenuPreferencePages() {
      String[] inheritedPages= super.collectContextMenuPreferencePages();
      int length= 2;
      String[] result= new String[inheritedPages.length + length];
      result[0]= "com.cthing.cmakeed.ui.prefs.PrefPage"; //$NON-NLS-1$
      result[1]= "com.cthing.cmakeed.ui.prefs.TemplatesPreferencePage"; //$NON-NLS-1$
      System.arraycopy(inheritedPages, 0, result, length, inheritedPages.length);
      return result;
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

    /* (non-Javadoc)
     * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#initializeKeyBindingScopes()
     */
    @Override
    protected void initializeKeyBindingScopes() {
      setKeyBindingScopes(new String[] { "com.cthing.cmakeed.ui.cmakeEditorScope" });  //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") final java.lang.Class key)
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
    @Override
    protected void initializeEditor() {
      IPreferenceStore store = new ChainedPreferenceStore(new IPreferenceStore[]{
          EditorsUI.getPreferenceStore(),
          CMakeEditorPlugin.getDefault().getPreferenceStore()
      });
      setPreferenceStore(store);
      store.addPropertyChangeListener(this);
      if (null == this.colorMgr) {
        this.colorMgr = new ColorMgr();
      }
      setSourceViewerConfiguration(new CMakeEditorConfiguration(this.colorMgr, store));
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
		CMakeEditorPlugin.getDefault().getCMakePartitionScanner().removeUserVariableRule();
	}


    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.editors.text.TextEditor#dispose()
     */
    @Override
    public void dispose()
    {
        final IPreferenceStore store = getPreferenceStore();
        store.removePropertyChangeListener(this);

        this.colorMgr.dispose();

        super.dispose();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent event)
    {
        if (event == null || Preferences.isTextPreference(event.getProperty())) {
            final SourceViewer sourceViewer = (SourceViewer)getSourceViewer();
            int line = sourceViewer.getTopIndex();
			sourceViewer.unconfigure();
            final CMakeEditorConfiguration config = new CMakeEditorConfiguration(this.colorMgr,
                getPreferenceStore());
            sourceViewer.configure(config);
            sourceViewer.refresh();
            sourceViewer.setTopIndex(line);
        }
    }

    /**
     * Get the source viewer, this method is useful for test cases.
     *
     * @return
     *      the source viewer
     */
    public SourceViewer getCMakeEditorSourceViewer() {
        return (SourceViewer) getSourceViewer();
    }

  @Override
  protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
    super.configureSourceViewerDecorationSupport(support);

    ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(matchChars,
        IDocumentExtension3.DEFAULT_PARTITIONING);
    support.setCharacterPairMatcher(matcher);
    support.setMatchingCharacterPainterPreferenceKeys(Preferences.MATCHING_BRACKETS_ON,
        Preferences.MATCHING_BRACKETS_COLOR);
    // track bracket highlighting in the preference store
    support.install(CMakeEditorPlugin.getDefault().getPreferenceStore());
  }
}
