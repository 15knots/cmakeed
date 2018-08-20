/**
 * Copyright 2008, BlueQuartz Software
 * Copyright 2018, Martin Weber
 */
package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;

import com.cthing.cmakeed.core.commands.CMakeCommand;
import com.cthing.cmakeed.core.commands.CMakeCommands;
import com.cthing.cmakeed.core.properties.CMakeProperties;
import com.cthing.cmakeed.core.properties.CMakeProperty;
import com.cthing.cmakeed.core.reservedwords.CMakeReservedWord;
import com.cthing.cmakeed.core.reservedwords.CMakeReservedWords;
import com.cthing.cmakeed.core.variables.CMakeVariable;
import com.cthing.cmakeed.core.variables.CMakeVariables;

/**
 * @author Michael Jackson for BlueQuartz Software
 *
 */
public class CMakeEditorTextHover implements ITextHover, ITextHoverExtension, ITextHoverExtension2
{

	private IInformationControlCreator hoverControlCreator;

        public CMakeEditorTextHover()
	{

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text.ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return new Region(offset, 0);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHoverExtension2#getHoverInfo2(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
	    return getHoverInfo(textViewer, hoverRegion);
	}

        /* (non-Javadoc)
         * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
         */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion)
	{
		//TODO: Load up the HTML file for the full command if found.
		try {
			IRegion wordRegion = getWordRegion(textViewer, hoverRegion);
			String word =  textViewer.getDocument().get(wordRegion.getOffset(), wordRegion.getLength());

			CMakeCommand cmd = CMakeCommands.getCommand(word);
			if (null != cmd) return format(cmd.getName() , cmd.getDescription(), cmd.getUsages());

			CMakeProperty prop = CMakeProperties.getCommand(word);
			if (null != prop) return format("CMake Property", prop.getDescription(), null);

			CMakeReservedWord resword = CMakeReservedWords.getCommand(word);
			if (null != resword)
			  return format("CMake Reserved Word" ,resword.getName(), null);

			CMakeVariable var = CMakeVariables.getCommand(word);
			if (null != var) return format("CMake Defined Variable", var.getDescription(), null);

			return null;
		} catch (Exception e) {
			return null;
		}
	}

  private String format(String name, String description, String[] usages) {
    String html;
    html = "<h2>" + name + "</h2>" + description;
    if (usages != null && usages.length > 0) {
      html += "<ul>";
      for (int i = 0; i < usages.length; i++) {
        html += "<li><b>"+ name + "</b>";
        html += usages[i].replaceAll("<", "&lt;") + "</li>";
      }
      html += "</ul>";
    }
    return html;
  }

	public IRegion getWordRegion(ITextViewer viewer, IRegion region)
	{
		int offset = region.getOffset();
		IDocument doc = viewer.getDocument();

		if (offset >= doc.getLength()) { return null; }
		int startOffset = offset;
		while( !EditorUtils.startOfWord(doc, startOffset) && startOffset >= 0 )
		{
			startOffset--;
		}

		int endOffset = offset;
		while( !EditorUtils.startOfWord(doc, endOffset) && endOffset < doc.getLength() )
		{
			endOffset++;
		}

		IRegion reg = new Region(startOffset, endOffset - startOffset - 1);
		return reg;
	}

  /* (non-Javadoc)
   * @see org.eclipse.jface.text.ITextHoverExtension#getHoverControlCreator()
   */
  public IInformationControlCreator getHoverControlCreator() {
    if (hoverControlCreator == null)
      hoverControlCreator= new HoverControlCreator();
    return hoverControlCreator;
  }

  private static final class HoverControlCreator extends AbstractReusableInformationControlCreator
      implements IPropertyChangeListener {

    private static final String BG_KEY = "org.eclipse.ui.workbench.HOVER_BACKGROUND";//$NON-NLS-1$
    private static final String FG_KEY = "org.eclipse.ui.workbench.HOVER_FOREGROUND";//$NON-NLS-1$

    private DefaultInformationControl iControl;

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.text.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.
     * widgets.Shell)
     */
    @Override
    protected IInformationControl doCreateInformationControl(Shell parent) {
      iControl = new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString());
      JFaceResources.getColorRegistry().addListener(this);
      setHoverColors();
      return iControl;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
      String property = event.getProperty();
      if (iControl != null && (property.equals(BG_KEY) || property.equals(FG_KEY))) {
        setHoverColors();
      }
    }

    private void setHoverColors() {
      ColorRegistry registry = JFaceResources.getColorRegistry();
      final Color foreground = registry.get(FG_KEY);
      if (foreground != null)
        iControl.setForegroundColor(foreground);
      final Color background = registry.get(BG_KEY);
      if (background != null)
        iControl.setBackgroundColor(background);
    }

    @Override
    public void widgetDisposed(DisposeEvent e) {
      super.widgetDisposed(e);
      // Called when active editor is closed.
      JFaceResources.getColorRegistry().removeListener(this);
    }
  }

}
