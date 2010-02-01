/**
 * Copyright 2008, BlueQuartz Software
 */
package com.cthing.cmakeed.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

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
public class CMakeEditorTextHover implements ITextHover {

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
	 * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) 
	{
		//TODO: Load up the HTML file for the full command if found.
		try {
			IRegion wordRegion = getWordRegion(textViewer, hoverRegion);
			String word =  textViewer.getDocument().get(wordRegion.getOffset(), wordRegion.getLength());
		
			CMakeCommand cmd = CMakeCommands.getCommand(word);
			if (null != cmd) return cmd.getName() + ": " + cmd.getDescription();
			
			CMakeProperty prop = CMakeProperties.getCommand(word);
			if (null != prop) return "CMake Property: " + prop.getDescription();
			
			CMakeReservedWord resword = CMakeReservedWords.getCommand(word);
			if (null != resword) return "CMake Reserved Word: " + resword.getName();
			
			CMakeVariable var = CMakeVariables.getCommand(word);
			if (null != var) return "CMake Defined Variable: " + var.getDescription();

			return null;
		} catch (Exception e) {
			return null;
		}
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
	
	
	
	
	
}
