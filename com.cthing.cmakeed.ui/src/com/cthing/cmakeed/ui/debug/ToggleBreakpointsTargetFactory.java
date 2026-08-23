/*******************************************************************************
 * Copyright (c) 2026 Martin Weber.
 *
 * Content is provided to you under the terms and conditions of the Eclipse Public License Version 2.0 "EPL".
 * A copy of the EPL is available at http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package com.cthing.cmakeed.ui.debug;

import java.util.Collections;
import java.util.Set;

import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTargetFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.editor.CMakeEditor;

/**
 * @author Martin Weber
 */
public class ToggleBreakpointsTargetFactory implements IToggleBreakpointsTargetFactory {
  public static final String FACTORY_ID = CMakeEditorPlugin.PLUGIN_ID + ".toggleBreakpointTarget";

  @Override
  public Set<String> getToggleTargets(IWorkbenchPart part, ISelection selection) {
    return isEditorPart(part) ? Collections.singleton(FACTORY_ID) : Collections.emptySet();
  }

  @Override
  public String getDefaultToggleTarget(IWorkbenchPart part, ISelection selection) {
    return isEditorPart(part) ? FACTORY_ID : null;
  }

  @Override
  public IToggleBreakpointsTarget createToggleTarget(String targetID) {
    if (FACTORY_ID.equals(targetID)) {
      return new BreakpointsTarget();
    }
    return null;
  }

  @Override
  public String getToggleTargetName(String targetID) {
    return "CMake Breakpoint";
  }

  @Override
  public String getToggleTargetDescription(String targetID) {
    return "Breakpoint for CMake lists file.";
  }

  private boolean isEditorPart(IWorkbenchPart part) {
    if (part instanceof CMakeEditor) {
      return true;
    }
    return false;
  }
}
