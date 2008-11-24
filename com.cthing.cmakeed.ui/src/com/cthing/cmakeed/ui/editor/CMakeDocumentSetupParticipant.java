/**
 * 
 */
package com.cthing.cmakeed.ui.editor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import com.cthing.cmakeed.ui.UIPlugin;
import com.cthing.cmakeed.ui.editor.CMakePartitionScanner;

/**
 * @author mjackson
 *
 */
public class CMakeDocumentSetupParticipant implements IDocumentSetupParticipant {

	public CMakeDocumentSetupParticipant() {
		
	}
	
	
	public void setup(IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			IDocumentPartitioner partitioner = new FastPartitioner(UIPlugin.getDefault().getCMakePartitionScanner(), CMakePartitionScanner.CMAKE_CONTENT_TYPES);
			extension3.setDocumentPartitioner(UIPlugin.CMAKE_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
	}

}
