/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.cthing.cmakeed.core.variables.CMakeVariable;
import com.cthing.cmakeed.core.variables.CMakeVariables;
import com.cthing.cmakeed.core.utils.StringUtils;
import com.cthing.cmakeed.ui.UIPlugin;

/**
 * Content assist for CMake commands.
 */
public class CMakeVariableAssist implements IContentAssistProcessor, ITextHover
{
    private final CMakeNameDetector detector = new CMakeNameDetector();

    /**
     * Default constructor for the class.
     */
    public CMakeVariableAssist()
    {
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
     */
    public ICompletionProposal[] computeCompletionProposals(final ITextViewer viewer,
                                                            final int offset)
    {
        final IDocument doc = viewer.getDocument();
        List<ICompletionProposal> proposals = null;
        
        if (EditorUtils.inArguments(doc, offset)) {
            final String prefix = getPrefix(doc, offset);
            final List<CMakeVariable> variables = findPossibleCommands(prefix);
            
            if (!StringUtils.isBlank(prefix) && !variables.isEmpty()) {
                final boolean isLowercase = Character.isLowerCase(prefix.charAt(0));
                final int replacementOffset = offset - prefix.length();
                final int replacementLength = prefix.length();

                proposals = new ArrayList<ICompletionProposal>();
                for (CMakeVariable variable : variables) {
                    final String name = isLowercase ? variable.getName().toUpperCase() : variable.getName();
                    final String desc = variable.getDescription();
                    final String[] usages = variable.getUsages();
                    
                    for (String usage : usages) {
                        final IContextInformation contextInfo =
                            new ContextInformation(name, usage);
                        proposals.add(new CompletionProposal(name + EditorUtils.START_ARGS,
                                replacementOffset, replacementLength,
                                name.length() + 1, null, name + usage,
                                contextInfo, desc));
                    }
                }
            }
        }
        
        return (proposals == null) ? null : proposals.toArray(new ICompletionProposal[proposals.size()]);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
     */
    public IContextInformation[] computeContextInformation(final ITextViewer viewer,
                                                           final int offset)
    {
        final IDocument doc = viewer.getDocument();
        final CMakeVariable cmd = EditorUtils.findContainingVariable(doc, offset);
        if (cmd != null) {

            final IContextInformation[] contexts = new IContextInformation[0];
            return contexts;
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
     */
    public IContextInformationValidator getContextInformationValidator()
    {
        return new IContextInformationValidator() {
            private IDocument doc;
            public void install(final IContextInformation info,
                                final ITextViewer viewer, final int offset)
            {
                this.doc = viewer.getDocument();
            }

            public boolean isContextInformationValid(final int offset)
            {
                boolean retval = false;
                
                try {
                    final String contentType = this.doc.getContentType(offset);
                    retval = CMakePartitionScanner.isAnyCommand(contentType) ||
                             CMakePartitionScanner.isArgsOpen(contentType) ||
                             CMakePartitionScanner.isString(contentType) ||
                             CMakePartitionScanner.isVariable(contentType) ||
                            (EditorUtils.findContainingVariable(this.doc, offset) != null );
                }
                catch (final BadLocationException e) {
                    UIPlugin.logError(this, e);
                }
                
                return retval;
            }
        };
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
     */
    public char[] getCompletionProposalAutoActivationCharacters()
    {
        return new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                            'u', 'v', 'w', 'x', 'y', 'z',
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                            'U', 'V', 'W', 'X', 'Y', 'Z', };
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationAutoActivationCharacters()
     */
    public char[] getContextInformationAutoActivationCharacters()
    {
        return new char[] { '(' };
    }
    
    /**
     * Determines the portion of the word that has already been entered.
     * 
     * @param doc  Document being edited
     * @param offset  Offset into the document where content assist is desired.
     * @return Portion of the word that has already been entered.
     */
    private String getPrefix(final IDocument doc, final int offset)
    {
        try {
            for (int n = offset - 1; n >= 0; n--) {
                final char c = doc.getChar(n);
                if (!this.detector.isWordPart(c)) {
                    return doc.get(n + 1, offset - n - 1);
                }
            }
        }
        catch (final BadLocationException e) {
            UIPlugin.logError(this, e);
        }
        
        return "";      //$NON-NLS-1$
    }
    
    /**
     * Determine possible commands that start with the specified prefix.
     * 
     * @param prefix  Prefix for commands
     * @return Commands that begin with the specified prefix.
     */
    private List<CMakeVariable> findPossibleCommands(final String prefix)
    {
        final List<CMakeVariable> possibles =
            new ArrayList<CMakeVariable>();
        final Collection<CMakeVariable> commands = CMakeVariables.getCommands();
        final String lowerPrefix = prefix.toUpperCase();
        
        for (CMakeVariable command : commands) {
            if (!command.isDeprecated()) {
                final String name = command.getName();
                if (name.startsWith(lowerPrefix)) {
                    possibles.add(command);
                }
                else if (!possibles.isEmpty()) {
                    break;
                }
            }
        }
        
        return possibles;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage()
     */
    public String getErrorMessage()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
     */
    public String getHoverInfo(final ITextViewer textViewer,
                               final IRegion hoverRegion)
    {
        final IDocument doc = textViewer.getDocument();
        String description = null;
        
        if (hoverRegion != null) {
            try {
                final String word = doc.get(hoverRegion.getOffset(),
                                            hoverRegion.getLength());
                final CMakeVariable command = CMakeVariables.getCommand(word);
                if (command != null) {
                    description = command.getDescription();
                }
            }
            catch (final BadLocationException e) {
                UIPlugin.logError(this, e);
            }
        }
        
        return description;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text.ITextViewer, int)
     */
    public IRegion getHoverRegion(final ITextViewer textViewer,
                                  final int offset)
    {
        IRegion region = null;
        
        try {
            region = textViewer.getDocument().getPartition(offset);
        }
        catch (final BadLocationException e) {
            UIPlugin.logError(this, e);
        }
        
        return region;
    }
}
