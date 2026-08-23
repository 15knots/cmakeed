/*******************************************************************************
 * Copyright (c) 2026 Martin Weber.
 *
 * Content is provided to you under the terms and conditions of the Eclipse Public License Version 2.0 "EPL".
 * A copy of the EPL is available at http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package com.cthing.cmakeed.ui.debug;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import com.cthing.cmakeed.ui.editor.CMakeEditor;

/**
 * @author Martin Weber
 */
public class BreakpointsTarget implements IToggleBreakpointsTarget {

  public BreakpointsTarget() {
  }

  @Override
  public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
    return true;
  }

  @Override
  public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
    return false;
  }

  @Override
  public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
    return false;
  }

  @Override
  public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
    if (part instanceof CMakeEditor) {
      IEditorPart editor = (IEditorPart) part;
      IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
      ITextSelection textSelection = (ITextSelection) selection;
      int lineNumber = textSelection.getStartLine();
      IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager()
          .getBreakpoints(ToggleBreakpointsTargetFactory.FACTORY_ID);
      for (IBreakpoint breakpoint : breakpoints) {
        if (breakpoint instanceof ILineBreakpoint && resource.equals(breakpoint.getMarker().getResource())) {
          if (((ILineBreakpoint) breakpoint).getLineNumber() == (lineNumber + 1)) {
            // remove
            breakpoint.delete();
            return;
          }
        }
      }
      IBreakpoint breakpoint = new Breakpoint(resource, lineNumber + 1);
      DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(breakpoint);
    }
  }

  @Override
  public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
  }

  @Override
  public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
  }
}
