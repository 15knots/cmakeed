/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.prefs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.cthing.cmakeed.ui.Messages;
import com.cthing.cmakeed.ui.UIPlugin;

/**
 * CMakeEd preference page.
 */
public class PrefPage extends PreferencePage
                            implements IWorkbenchPreferencePage
{
    private Map<String, RGB> colorMap = new HashMap<String, RGB>();
    private Map<String, Integer> styleMap = new HashMap<String, Integer>();
    
    private Button spacesForTabsB;
    private ListViewer textAttrViewer;
    private ColorSelector colorB;
    private Button boldB;
    private Button italicB;
    private Button underlineB;
    private Button strikeB;
    
    /**
     * Default constructor for the class.
     */
    public PrefPage()
    {
        setPreferenceStore(UIPlugin.getDefault().getPreferenceStore());
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(final Composite parent)
    {
        final Composite topComp = new Composite(parent, SWT.NONE);
        topComp.setLayout(new GridLayout(1, false));
        
        // Header
        final Control control = createHeader(topComp);
        control.setLayoutData(new GridData());
        
        // Spaces for tabs
        final Composite spacesComp = new Composite(topComp, SWT.NONE);
        spacesComp.setLayoutData(new GridData());
        spacesComp.setLayout(new GridLayout(2, false));
        
        this.spacesForTabsB = new Button(spacesComp, SWT.CHECK);
        this.spacesForTabsB.setLayoutData(new GridData());
        final Label spacesForTabsL = new Label(spacesComp, SWT.NONE);
        spacesForTabsL.setLayoutData(new GridData());
        spacesForTabsL.setText(Messages.getString("PreferencePage.SpacesForTabs")); //$NON-NLS-1$
        
        // Text attributes
        final Composite textComp = new Composite(topComp, SWT.NONE);
        textComp.setLayoutData(new GridData());
        textComp.setLayout(new GridLayout(2, false));
        
        //      List of text attributes
        final Label attrL = new Label(textComp, SWT.NONE);
        final GridData attrData = new GridData();
        attrData.horizontalSpan = 2;
        attrL.setLayoutData(attrData);
        attrL.setText(Messages.getString("PreferencePage.Attributes"));    //$NON-NLS-1$
        
        this.textAttrViewer = new ListViewer(textComp, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
        this.textAttrViewer.getList().setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
                                                                false, false));
        this.textAttrViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(final Object element)
            {
                return Preferences.getMessage(element);
            }
        });
        this.textAttrViewer.setContentProvider(new ArrayContentProvider());
        this.textAttrViewer.setSorter(new ViewerSorter() {
            @Override
            public int compare(final Viewer viewer, final Object obj1,
                               final Object obj2)
            {
                final String label1 = Preferences.getMessage(obj1);
                final String label2 = Preferences.getMessage(obj2);
                return label1.toString().compareToIgnoreCase(label2.toString());
            }
        });
        this.textAttrViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event)
            {
                writeTextUI();
            }
        });
        
        //      Text styles
        final Composite styleComp = new Composite(textComp, SWT.NONE);
        styleComp.setLayoutData(new GridData(SWT.NONE, SWT.BEGINNING,
                                             false, false));
        styleComp.setLayout(new GridLayout(2, false));
        
        //              Text color
        final Label colorL = new Label(styleComp, SWT.NONE);
        colorL.setLayoutData(new GridData(SWT.END, SWT.NONE, false, false));
        colorL.setText(Messages.getString("PreferencePage.Color")); //$NON-NLS-1$
        
        this.colorB = new ColorSelector(styleComp);
        this.colorB.getButton().setLayoutData(new GridData());
        this.colorB.addListener(new IPropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent event)
            {
                readTextUI();
            }
        });
        
        //              Text typographics
        this.boldB = new Button(styleComp, SWT.CHECK);
        this.boldB.setLayoutData(new GridData(SWT.END, SWT.NONE, false, false));
        this.boldB.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                readTextUI();
            }
        });
        final Label boldL = new Label(styleComp, SWT.NONE);
        boldL.setLayoutData(new GridData());
        boldL.setText(Messages.getString("PreferencePage.Bold"));   //$NON-NLS-1$
        
        this.italicB = new Button(styleComp, SWT.CHECK);
        this.italicB.setLayoutData(new GridData(SWT.END, SWT.NONE, false, false));
        this.italicB.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                readTextUI();
            }
        });
        final Label italicL = new Label(styleComp, SWT.NONE);
        italicL.setLayoutData(new GridData());
        italicL.setText(Messages.getString("PreferencePage.Italic"));   //$NON-NLS-1$
        
        this.underlineB = new Button(styleComp, SWT.CHECK);
        this.underlineB.setLayoutData(new GridData(SWT.END, SWT.NONE, false, false));
        this.underlineB.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                readTextUI();
            }
        });
        final Label underlineL = new Label(styleComp, SWT.NONE);
        underlineL.setLayoutData(new GridData());
        underlineL.setText(Messages.getString("PreferencePage.Underline"));   //$NON-NLS-1$
        
        this.strikeB = new Button(styleComp, SWT.CHECK);
        this.strikeB.setLayoutData(new GridData(SWT.END, SWT.NONE, false, false));
        this.strikeB.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                readTextUI();
            }
        });
        final Label strikeL = new Label(styleComp, SWT.NONE);
        strikeL.setLayoutData(new GridData());
        strikeL.setText(Messages.getString("PreferencePage.Strike"));   //$NON-NLS-1$
        
        init(UIPlugin.getDefault().getWorkbench());
        
        return topComp;
    }
    
    /**
     * Creates the preference header hyper link label indicating that
     * additional preferences are available on the Text Editors preference
     * page.
     * 
     * @param parent  Parent container
     * @return The created control
     */
    private Control createHeader(final Composite parent)
    {
        final Link link = new Link(parent, SWT.NONE);
        link.setText(Messages.getString("PreferencePage.AdditionalPrefs")); //$NON-NLS-1$
        final String target = "org.eclipse.ui.preferencePages.GeneralTextEditor"; //$NON-NLS-1$
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event)
            {
                PreferencesUtil.createPreferenceDialogOn(link.getShell(), target, null, null);
            }
        });
        
        final String linktooltip = Messages.getString("PreferencePage.AdditionalTip"); //$NON-NLS-1$
        link.setToolTipText(linktooltip);
        
        return link;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(final IWorkbench workbench)
    {   
        if (this.textAttrViewer != null) {
            this.textAttrViewer.setInput(Preferences.TEXT_KEYS);
            this.textAttrViewer.getList().select(0);
            this.textAttrViewer.getList().showSelection();

            final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();            
            this.spacesForTabsB.setSelection(store.getBoolean(Preferences.SPACES_FOR_TABS));
            
            readTextPrefs();
            writeTextUI();
        }
    }
    
    /**
     * Obtains the current text attribute selection.
     * 
     * @return Current text attribute selection. This is the base preference
     *      key.
     */
    private String getSelection()
    {
        final IStructuredSelection selection =
            (IStructuredSelection)this.textAttrViewer.getSelection();
        return (String)selection.getFirstElement();
    }
    
    /**
     * Reads the text preferences into the local style.
     */
    private void readTextPrefs()
    {
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
        
        for (String baseKey : Preferences.TEXT_KEYS) {
            this.colorMap.put(baseKey, PreferenceConverter.getColor(store,
                    Preferences.getColorKey(baseKey)));
            this.styleMap.put(baseKey,
                    store.getInt(Preferences.getStyleKey(baseKey)));
        }
    }
    
    /**
     * Writes the text preferences from the local style.
     */
    private void writeTextPrefs()
    {
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
        
        for (String baseKey : Preferences.TEXT_KEYS) {
            PreferenceConverter.setValue(store, Preferences.getColorKey(baseKey), this.colorMap.get(baseKey));
            store.setValue(Preferences.getStyleKey(baseKey), this.styleMap.get(baseKey));
            
            
            this.colorMap.put(baseKey, PreferenceConverter.getColor(store,
                    Preferences.getColorKey(baseKey)));
            this.styleMap.put(baseKey,
                    store.getInt(Preferences.getStyleKey(baseKey)));
        }
    }
    
    /**
     * Updates the text UI based on the local style.
     */
    private void writeTextUI()
    {
        final String baseKey = getSelection();

        this.colorB.setColorValue(this.colorMap.get(baseKey));
        
        final int style = this.styleMap.get(baseKey);
        this.boldB.setSelection((style & SWT.BOLD) != 0);
        this.italicB.setSelection((style & SWT.ITALIC) != 0);
        this.underlineB.setSelection((style & TextAttribute.UNDERLINE) != 0);
        this.strikeB.setSelection((style & TextAttribute.STRIKETHROUGH) != 0);
    }
    
    /**
     * Reads the style from the UI and sets the local style.
     */
    private void readTextUI()
    {
        final String baseKey = getSelection();
        
        final RGB color = this.colorB.getColorValue();
        this.colorMap.put(baseKey, color);
        
        final int bold = this.boldB.getSelection() ? SWT.BOLD : SWT.NONE;
        final int italic = this.italicB.getSelection() ? SWT.ITALIC : SWT.NONE;
        final int underline = this.underlineB.getSelection() ? TextAttribute.UNDERLINE : SWT.NONE;
        final int strike = this.strikeB.getSelection() ? TextAttribute.STRIKETHROUGH : SWT.NONE;
        final int style = bold | italic | underline | strike;
        this.styleMap.put(baseKey, style);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults()
    {
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
        
        for (String baseKey : Preferences.TEXT_KEYS) {
            this.colorMap.put(baseKey, PreferenceConverter.getDefaultColor(store,
                    Preferences.getColorKey(baseKey)));
            this.styleMap.put(baseKey,
                    store.getDefaultInt(Preferences.getStyleKey(baseKey)));
        }
        
        writeTextUI();

        super.performDefaults();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk()
    {
        final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();            
        store.setValue(Preferences.SPACES_FOR_TABS, this.spacesForTabsB.getSelection());

        writeTextPrefs();
        return super.performOk();
    }
}
