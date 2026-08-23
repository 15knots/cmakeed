/*******************************************************************************
 * Copyright (c) 2026 Martin Weber.
 *
 * Content is provided to you under the terms and conditions of the Eclipse Public License Version 2.0 "EPL".
 * A copy of the EPL is available at http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package com.cthing.cmakeed.ui.debug;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;

/**
 * @author Martin Weber
 */
public class Breakpoint extends org.eclipse.debug.core.model.LineBreakpoint {
  // from plugin.xml
  public static final String MARKER_ID = "com.cthing.cmakeed.ui.debug.breakpoint.marker";

  public Breakpoint() {
  }

  public Breakpoint(final IResource resource, final int lineNumber) throws CoreException {
    run(getMarkerRule(resource), monitor -> {
      IMarker marker = resource.createMarker(MARKER_ID);
      setMarker(marker);
      marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
      marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
      marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
      marker.setAttribute(IMarker.MESSAGE, resource.getName() + " [line: " + lineNumber + "]");
    });
  }

  @Override
  public String getModelIdentifier() {
    return ToggleBreakpointsTargetFactory.FACTORY_ID;
  }
}
