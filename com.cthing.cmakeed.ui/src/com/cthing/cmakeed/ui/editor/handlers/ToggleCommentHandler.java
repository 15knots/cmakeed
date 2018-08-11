/*******************************************************************************
 * Copyright (c) 2018 Martin Weber.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Martin Weber - Initial implementation
 *******************************************************************************/

package com.cthing.cmakeed.ui.editor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.editor.CMakeEditor;

/**
 * Toggles single line comments in CMakeListsts.txt source files.
 *
 * @author Martin Weber
 */
public class ToggleCommentHandler extends AbstractHandler {

  /** the character to prefix a source line with to uncomment it */
  public static final char COMMENT_CHAR = '#';

  /*
   * (non-Javadoc)
   *
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    final ISelection currentSelection = HandlerUtil.getCurrentSelectionChecked(event);
    final IEditorPart activeEditor = HandlerUtil.getActiveEditorChecked(event);
    if (currentSelection instanceof ITextSelection && activeEditor instanceof CMakeEditor) {
      ITextSelection selection = (ITextSelection) currentSelection;
      CMakeEditor editor = (CMakeEditor) activeEditor;

      if (selection.getStartLine() < 0 || selection.getEndLine() < 0)
        return null;

      IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
      try {
        SourceViewer sourceViewer = editor.getCMakeEditorSourceViewer();
        final int offset = document.getLineOffset(selection.getStartLine());
        // check first char in first selected line for comment char
        final int opCode = document.getChar(offset) == COMMENT_CHAR ? ITextOperationTarget.STRIP_PREFIX
            : ITextOperationTarget.PREFIX;
        if (sourceViewer.canDoOperation(opCode)) {
          sourceViewer.doOperation(opCode);
        }
      } catch (final BadLocationException e) {
        CMakeEditorPlugin.logError(this, e);
      }
    }
    return null;
  }

}
