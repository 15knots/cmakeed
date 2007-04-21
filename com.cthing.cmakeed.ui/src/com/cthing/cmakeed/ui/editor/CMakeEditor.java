/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

import com.cthing.cmakeed.ui.Messages;
import com.cthing.cmakeed.ui.UIPlugin;
import com.cthing.cmakeed.ui.prefs.Preferences;

/**
 * Editor for CMake files.
 */
public class CMakeEditor extends AbstractDecoratedTextEditor
                         implements IPropertyChangeListener
{
    private StyledText text;
    private ColorMgr colorMgr;

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
    public CMakeEditor()
    {
        this.colorMgr = new ColorMgr();
        
        final IDocumentProvider provider = new CMakeDocumentProvider();
        setDocumentProvider(provider);
        final CMakeEditorConfiguration config =
            new CMakeEditorConfiguration(this.colorMgr);
        setSourceViewerConfiguration(config);
        
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
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
    public Object getAdapter(final Class key)
    {
        if (key.equals(ISourceViewer.class)) {
            return getSourceViewer();
        }

        if (key.equals(StyledText.class)) {
            return this.text;
        }
        
        return super.getAdapter(key);
    }
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.editors.text.TextEditor#dispose()
     */
    @Override
    public void dispose()
    {
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
        store.removePropertyChangeListener(this);
     
        this.colorMgr.dispose();
        
        super.dispose();
    }
    
    /**
     * Adds or removes the tab replacer based on the preference setting.
     */
    private void updateTabReplacer()
    {
        final IPreferenceStore prefStore = UIPlugin.getDefault().getPreferenceStore();
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
        if (event == null ||
                event.getProperty().equals(Preferences.SPACES_FOR_TABS)) {
            updateTabReplacer();
        }
        
        if (event == null || Preferences.isTextPreference(event.getProperty())) {
            final SourceViewer sourceViewer = (SourceViewer)getSourceViewer();
            sourceViewer.unconfigure();
            final CMakeEditorConfiguration config =
                new CMakeEditorConfiguration(this.colorMgr);
            sourceViewer.configure(config);
            sourceViewer.refresh();
        }
    }
}
