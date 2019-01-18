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
    final CMakeEditorUI editorUI = CMakeEditorUI.getDefault();
    setPreferenceStore(editorUI.getPreferenceStore());
    setTemplateStore(editorUI.getTemplateStore());
    setContextTypeRegistry(editorUI.getContextTypeRegistry());
  }

	protected boolean isShowFormatterSetting() {
		return false;
	}
}
