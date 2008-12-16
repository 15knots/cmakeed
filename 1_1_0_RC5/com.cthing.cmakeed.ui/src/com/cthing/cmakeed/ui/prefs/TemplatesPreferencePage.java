/*
 * Copyright 2008 Michael A. Jackson: BlueQuartz Software
 * http://www.bluequartz.net All Rights Reserved.
 */
package com.cthing.cmakeed.ui.prefs;

import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

import com.cthing.cmakeed.ui.editor.CMakeEditorUI;

/**
 * @author mjackson
 *
 */
public class TemplatesPreferencePage extends TemplatePreferencePage {

	/**
	 * 
	 */
	public TemplatesPreferencePage() {
		setPreferenceStore(CMakeEditorUI.getDefault().getPreferenceStore());
		setTemplateStore(CMakeEditorUI.getDefault().getTemplateStore());
		setContextTypeRegistry(CMakeEditorUI.getDefault().getContextTypeRegistry());
	}

	protected boolean isShowFormatterSetting() {
		return false;
	}


	public boolean performOk() {
		boolean ok= super.performOk();

		CMakeEditorUI.getDefault().savePluginPreferences();

		return ok;
	}

}
