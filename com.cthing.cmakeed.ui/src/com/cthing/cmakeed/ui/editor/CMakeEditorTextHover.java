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
 * @author Martin Weber
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
      hoverControlCreator= new HoverControlCreator(false);
    return hoverControlCreator;
  }

  /////////////////////////////////////////////////////////////////////////////
  /**
   * @author Martin Weber
   */
  private static final class HoverControlCreator extends AbstractReusableInformationControlCreator {

    private final boolean createEnriched;

    /**
     * @param createEnriched
     */
    public HoverControlCreator(boolean createEnriched) {
      this.createEnriched = createEnriched;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.text.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.
     * widgets.Shell)
     */
    @Override
    protected IInformationControl doCreateInformationControl(Shell parent) {
      if(createEnriched)
        return new InformationControl(parent, true);
      else
        return new InformationControl(parent, EditorsUI.getTooltipAffordanceString());
    }
  } // HoverControlCreator

  /**
   * Used when the control gets enriched.
   *
   * @author Martin Weber
   */
  private static final class InformationControl extends DefaultInformationControl {
    private static final String BG_KEY = "org.eclipse.ui.workbench.HOVER_BACKGROUND";//$NON-NLS-1$
    private static final String FG_KEY = "org.eclipse.ui.workbench.HOVER_FOREGROUND";//$NON-NLS-1$

    private InformationControl(Shell parent, String statusFieldText) {
      super(parent, statusFieldText);
      init();
    }

    private InformationControl(Shell parent, boolean isResizeable) {
      super(parent, isResizeable);
      init();
    }

    private void init() {
      ColorRegistry registry = JFaceResources.getColorRegistry();
      final Color foreground = registry.get(FG_KEY);
      if (foreground != null)
        setForegroundColor(foreground);
      final Color background = registry.get(BG_KEY);
      if (background != null)
        setBackgroundColor(background);
    }

    /** Overwritten to return a control which is resizeable. */
    @Override
    public IInformationControlCreator getInformationPresenterControlCreator() {
      return new HoverControlCreator(true);
    }
  } // InformationControl

}
