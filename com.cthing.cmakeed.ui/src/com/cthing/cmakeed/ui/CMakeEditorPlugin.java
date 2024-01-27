/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.cthing.cmakeed.core.utils.ClassUtils;
import com.cthing.cmakeed.ui.editor.CMakePartitionScanner;

/**
 * Activator class controlling the plug-in life cycle.
 */
public class CMakeEditorPlugin extends AbstractUIPlugin
{
    /** Plugin identifier. */
    public static final String PLUGIN_ID = "com.cthing.cmakeed.ui";     //$NON-NLS-1$

	public static final String CMAKE_PARTITIONING = "__cmake_partitioning"; //$NON-NLS-1$

    // The shared instance
    private static CMakeEditorPlugin plugin;

    private CMakePartitionScanner fPartitionScanner;

    /**
     * Default constructor for the class.
     */
    public CMakeEditorPlugin()
    {
        plugin = this;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(final BundleContext context) throws Exception
    {
        super.start(context);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(final BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the instance of the plugin.
     *
     * @return Instance of the plugin.
     */
    public static CMakeEditorPlugin getDefault()
    {
        return plugin;
    }

    public CMakePartitionScanner getCMakePartitionScanner() {
		if (fPartitionScanner == null)
			fPartitionScanner= new CMakePartitionScanner();
		return fPartitionScanner;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
     * Logs an informational message to the plugin log.
     *
     * @param obj  Object issuing the message.
     * @param message  Message to be logged.
     */
    public static void logInfo(final Object obj, final String message)
    {
        log(obj, 3, IStatus.INFO, message, null);
    }

    /**
     * Logs an error message to the plugin log.
     *
     * @param obj  Object issuing the message.
     * @param e  Exception; may be <code>null</code>
     */
    public static void logError(final Object obj, final Throwable e)
    {
        log(obj, 4, IStatus.ERROR, e.getMessage(), e);
    }

    /**
     * Logs an error message to the plugin log.
     *
     * @param obj  Object issuing the message.
     * @param message  Error message; may be <code>null</code>.
     * @param e  Exception; may be <code>null</code>
     */
    public static void logError(final Object obj, final String message,
                               final Throwable e)
    {
        log(obj, 3, IStatus.ERROR, message, e);
    }

    /**
     * Logs a plugin message.
     *
     * @param obj  Object issuing the message
     * @param callLevel  Stack level to identify calling method
     * @param severity  One of the IStatus message severity codes
     * @param message  Message to log; may be <code>null</code>
     * @param e  Exception; may be <code>null</code>
     */
    private static void log(final Object obj, final int callLevel,
                            final int severity, final String message,
                            final Throwable e)
    {
        final StringBuilder msg = new StringBuilder();
        msg.append(ClassUtils.getShortClassName((obj instanceof Class) ? (Class<?>)obj : obj.getClass()));
        msg.append("."); //$NON-NLS-1$
        msg.append(ClassUtils.getMethodName(callLevel));
        msg.append(": "); //$NON-NLS-1$
        if (message != null) {
            msg.append(message);
        }

        final Status status = new Status(severity, PLUGIN_ID, severity,
                                         msg.toString(), e);
        plugin.getLog().log(status);
    }

    /**
     * Gets a translated string from the plugins localization (plugin.properties).
     *
     * @param key
     *            the key for the translated string
     */
    public static String getResourceString(String key) {
      return Platform.getResourceString(plugin.getBundle(), "%" + key);
    }
}
