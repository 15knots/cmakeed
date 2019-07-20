/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import junit.framework.TestCase;

import com.cthing.cmakeed.ui.editor.CMakeEditor;

/**
 * Common base class for CMake editor test cases.
 */
@SuppressWarnings("nls")
public class EditorTestCase extends TestCase
{
    private static final String TEST_CMAKE_FILE = "/com/cthing/cmakeed/ui/CMakeLists.txt";

    private IFile file;
    private IDocument document;
    private CMakeEditor editor;
    private ISourceViewer viewer;

    /**
     * Default constructor for the class.
     */
    public EditorTestCase()
    {
    }

    /**
     * @return The CMakeLists.txt file.
     */
    public IFile getFile()
    {
        return this.file;
    }

    /**
     * @return The document containing the CMakeLists.txt file.
     */
    public IDocument getDocument()
    {
        return this.document;
    }

    /**
     * @return The CMakeEditor.
     */
    public CMakeEditor getEditor()
    {
        return this.editor;
    }

    /**
     * @return The source viewer hosting the document.
     */
    public ISourceViewer getViewer()
    {
        return this.viewer;
    }

    /**
     * {@inheritDoc}
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        if (this.editor == null) {
            try {
                final IWorkspace ws = ResourcesPlugin.getWorkspace();
                final IProject project = ws.getRoot().getProject("TestFiles");
                if (project.exists()) {
                    project.open(null);
                }
                else {
                    project.create(null);
                    project.open(null);
                }

                final URL url = FileLocator.resolve(getClass().getResource(TEST_CMAKE_FILE));
                final IPath location = new Path(url.getPath());
                this.file = project.getFile(location.lastSegment());
                this.file.createLink(location, IResource.REPLACE, null);

                final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                final IEditorPart ed = IDE.openEditor(page, this.file);

                if (ed instanceof CMakeEditor) {
                    this.editor = (CMakeEditor)ed;
                    this.viewer = (ISourceViewer) this.editor.getAdapter(ISourceViewer.class);
                    this.document = this.viewer.getDocument();
                }
                else {
                    fail("Error creating CMakeEditor");
                }
            }
            catch (final CoreException e) {
                fail(e.getMessage());
            }
            catch (final IOException e) {
                fail(e.getMessage());
            }
        }
    }
}
