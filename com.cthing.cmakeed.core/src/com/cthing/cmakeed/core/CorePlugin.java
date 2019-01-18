/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import com.cthing.cmakeed.core.utils.ClassUtils;

/**
 * Activator class controling the plug-in life cycle.
 */
public class CorePlugin extends Plugin
{
    /** Plugin identifier. */
    public static final String PLUGIN_ID = "com.cthing.cmakeed.core";   //$NON-NLS-1$

    // The shared instance
    private static CorePlugin plugin;

    /**
     * Default constructor for the class.
     */
    public CorePlugin()
    {
        plugin = this;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(final BundleContext context) throws Exception
    {
        super.start(context);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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
    public static CorePlugin getDefault()
    {
        return plugin;
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
}
