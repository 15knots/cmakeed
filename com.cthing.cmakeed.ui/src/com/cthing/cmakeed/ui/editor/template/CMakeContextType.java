/*
 * Copyright 2008 Michael A. Jackson: BlueQuartz Software
 * http://www.bluequartz.net All Rights Reserved.
 */

package com.cthing.cmakeed.ui.editor.template;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;

/**
 * @author Michael A. Jackson: BlueQuartz Software
 *
 */
public class CMakeContextType extends TemplateContextType {


	/** This context's id */
	public static final String CMAKE_CONTEXT_TYPE= "com.cthing.cmakeed.ui.editor.cmake"; //$NON-NLS-1$

	/**
	 * Creates a new XML context type.
	 */
	public CMakeContextType() {
		addGlobalResolvers();
	}

	private void addGlobalResolvers() {
		addResolver(new GlobalTemplateVariables.Cursor());
		addResolver(new GlobalTemplateVariables.WordSelection());
		addResolver(new GlobalTemplateVariables.LineSelection());
		addResolver(new GlobalTemplateVariables.Dollar());
		addResolver(new GlobalTemplateVariables.Date());
		addResolver(new GlobalTemplateVariables.Year());
		addResolver(new GlobalTemplateVariables.Time());
		addResolver(new GlobalTemplateVariables.User());
	}

	
}
