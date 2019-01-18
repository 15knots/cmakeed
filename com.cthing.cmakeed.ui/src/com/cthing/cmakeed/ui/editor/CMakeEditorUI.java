package com.cthing.cmakeed.ui.editor;


import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;

import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;

import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.editor.template.CMakeContextType;

import org.eclipse.ui.plugin.AbstractUIPlugin;



/**
 * The main plugin class to be used in the desktop.
 */
public class CMakeEditorUI  {
	/** Key to store custom templates. */
	private static final String CUSTOM_TEMPLATES_KEY= "com.cthing.cmakeed.ui.customtemplates"; //$NON-NLS-1$

	/** The shared instance. */
	private static CMakeEditorUI fInstance;

	/** The template store. */
	private TemplateStore fStore;
	/** The context type registry. */
	private ContributionContextTypeRegistry fRegistry;

	private CMakeEditorUI() {
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static CMakeEditorUI getDefault() {
		if (fInstance == null)
			fInstance= new CMakeEditorUI();
		return fInstance;
	}

	/**
	 * Returns this plug-in's template store.
	 *
	 * @return the template store of this plug-in instance
	 */
	public TemplateStore getTemplateStore() {
		if (fStore == null) {
			fStore= new ContributionTemplateStore(getContextTypeRegistry(), CMakeEditorPlugin.getDefault().getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
			try {
				fStore.load();
			} catch (IOException e) {
				CMakeEditorPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, "com.cthing.cmakeed.ui", IStatus.OK, "", e)); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return fStore;
	}

	/**
	 * Returns this plug-in's context type registry.
	 *
	 * @return the context type registry for this plug-in instance
	 */
	public ContextTypeRegistry getContextTypeRegistry() {
		if (fRegistry == null) {
			// create an configure the contexts available in the template editor
			fRegistry= new ContributionContextTypeRegistry();
			fRegistry.addContextType(CMakeContextType.CMAKE_CONTEXT_TYPE);
		}
		return fRegistry;
	}

	/* Forward plug-in methods to javaeditor example plugin default instance */
	public ImageRegistry getImageRegistry() {
		return CMakeEditorPlugin.getDefault().getImageRegistry();
	}

	public static ImageDescriptor imageDescriptorFromPlugin(String string, String default_image) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(string, default_image);
	}

	public IPreferenceStore getPreferenceStore() {
		return CMakeEditorPlugin.getDefault().getPreferenceStore();
	}

}
