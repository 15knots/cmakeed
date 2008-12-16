/*
 * Copyright 2008 Michael A. Jackson: BlueQuartz Software
 * http://www.bluequartz.net All Rights Reserved.
 */
package com.cthing.cmakeed.ui.editor.actions;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.editor.CMakeEditor;

import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * 
 * @author mjackson
 *
 */
public class CMakeEditorUnCommentActionDelegate implements IEditorActionDelegate, IWorkbenchWindowActionDelegate {

    CMakeEditor editor;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
     * .IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(final IAction action, final ISelection selection) {
        if (CMakeEditorPlugin.getActiveEditor() instanceof CMakeEditor)
            editor = (CMakeEditor) CMakeEditorPlugin.getActiveEditor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(final IAction action) {
        final IDocument document = editor.getCMakeEditorSourceViewer().getDocument();
        final ISelection currentSelection = editor.getCMakeEditorSourceViewer().getSelection();
        if (currentSelection instanceof ITextSelection) {
            final ITextSelection selection = (ITextSelection) currentSelection;
          //  String selectedContent = "";
            try {
                int begin = document.getLineOffset(selection.getStartLine());
                int end = document.getLineOffset(selection.getEndLine() + 1);
                // Get everything in the document up to the beginning of the line of first line of the selection
                final StringBuilder sb = new StringBuilder(document.get().substring(0, begin));
                // Get the actual selected text by full lines, including the new line chars at the end of each line
                String content = document.get().substring(begin, end);
                
                final LineNumberReader reader = new LineNumberReader(new StringReader(content));
                String line;
                String trimmedLine;
                int charsAdded = 0;
                try {
                    line = reader.readLine();
                    while ( line != null) {
                        trimmedLine = line.trim();
                        if (trimmedLine.startsWith("#") == true)
                        {
                            line = line.replaceFirst("#", "");
                            sb.append(line).append('\n');
                            charsAdded++;
                        }
                        else {
                            sb.append(line).append('\n');
                        }
                        line = reader.readLine();
                    }
                } catch (final IOException e) {
                    CMakeEditorPlugin.logError(this, e);
                }
                sb.append(document.get().substring(end, document.get().length()));
                document.set(sb.toString());
                int length = selection.getLength() - charsAdded;
                if (length < 0) { length = 0; }
                ITextSelection endSelection = new TextSelection(selection.getOffset(), length);
                editor.getSelectionProvider().setSelection(endSelection);
            } catch (final BadLocationException e) {
                CMakeEditorPlugin.logError(this, e);
            }
        }
    }

    /**
     * Check if all lines are commented
     * 
     * @param content
     *            to check
     * @return true if all lines begin with '#' char
     */
    private boolean linesContentCommentChar(final String content) {
        if (content.length() == 0) { return false; }
        final LineNumberReader reader = new LineNumberReader(new StringReader(content));
        String line;
        boolean ret = false;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#"))
                    ret = false;
                else
                    return true;
            }
        } catch (final IOException e) {
            CMakeEditorPlugin.logError(this, e);
            return false;
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose() {

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
     * IWorkbenchWindow)
     */
    public void init(final IWorkbenchWindow window) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface
     * .action.IAction, org.eclipse.ui.IEditorPart)
     */
    public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {

    }
}
