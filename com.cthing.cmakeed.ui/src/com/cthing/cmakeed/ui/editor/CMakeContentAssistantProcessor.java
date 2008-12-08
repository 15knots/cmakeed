/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
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
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;


import com.cthing.cmakeed.core.commands.CMakeCommand;
import com.cthing.cmakeed.core.commands.CMakeCommands;
import com.cthing.cmakeed.core.properties.CMakeProperties;
import com.cthing.cmakeed.core.properties.CMakeProperty;
import com.cthing.cmakeed.core.reservedwords.CMakeReservedWord;
import com.cthing.cmakeed.core.reservedwords.CMakeReservedWords;
import com.cthing.cmakeed.core.uservariables.CMakeUserVariable;
import com.cthing.cmakeed.core.uservariables.CMakeUserVariables;
import com.cthing.cmakeed.core.utils.StringUtils;
import com.cthing.cmakeed.core.variables.CMakeVariable;
import com.cthing.cmakeed.core.variables.CMakeVariables;
import com.cthing.cmakeed.ui.CMakeEditorPlugin;
import com.cthing.cmakeed.ui.editor.rules.CMakeUserVariableRule;
import com.cthing.cmakeed.ui.editor.template.CMakeContextType;

/**
 * Content assist for CMake commands.
 */
public class CMakeContentAssistantProcessor extends TemplateCompletionProcessor 
											implements IContentAssistProcessor, ITextHover
{

    private final CMakeNameDetector detector = new CMakeNameDetector();
    private static final String DEFAULT_IMAGE= "$nl$/icons/template.gif"; //$NON-NLS-1$
    
    /**
     * Default constructor for the class.
     */
    public CMakeContentAssistantProcessor()
    {
    }
    
/* -------------------------------------------------------------------------- */    
/* Start TemplateCompletionProcessor Implementation    */
/* -------------------------------------------------------------------------- */
	/**
	 * Simply return all templates.
	 *
	 * @param contextTypeId the context type, ignored in this implementation
	 * @return all templates
	 */
	protected Template[] getTemplates(String contextTypeId) {
		return CMakeEditorUI.getDefault().getTemplateStore().getTemplates();
	}

	/**
	 * Return the XML context type that is supported by this plug-in.
	 *
	 * @param viewer the viewer, ignored in this implementation
	 * @param region the region, ignored in this implementation
	 * @return the supported XML context type
	 */
	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		return CMakeEditorUI.getDefault().getContextTypeRegistry().getContextType(CMakeContextType.CMAKE_CONTEXT_TYPE);
	}

	/**
	 * Always return the default image.
	 *
	 * @param template the template, ignored in this implementation
	 * @return the default template image
	 */
	protected Image getImage(Template template) {
		ImageRegistry registry= CMakeEditorUI.getDefault().getImageRegistry();
		Image image= registry.get(DEFAULT_IMAGE);
		if (image == null) {
			ImageDescriptor desc= CMakeEditorUI.imageDescriptorFromPlugin("org.eclipse.ui.examples.javaeditor", DEFAULT_IMAGE); //$NON-NLS-1$
			registry.put(DEFAULT_IMAGE, desc);
			image= registry.get(DEFAULT_IMAGE);
		}
		return image;
	}
    
    
    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
     */
    public ICompletionProposal[] computeCompletionProposals( ITextViewer viewer,
                                                             int offset)
    {    	
        IDocument doc = viewer.getDocument();
        String prefix = getPrefix(doc, offset);

        ICompletionProposal[] templateArray = super.computeCompletionProposals(viewer, offset);
        List<ICompletionProposal> proposals = findPossibleTemplates(prefix, templateArray);

        if (!EditorUtils.inArguments(doc, offset)) {
        //-- Get any command proposals	
            final List<CMakeCommand> commands = findPossibleCommands(prefix);
            
            if ( /*!StringUtils.isBlank(prefix) && */ !commands.isEmpty()) {
                boolean isLowercase = false;
                if (prefix.length() > 0) { Character.isLowerCase(prefix.charAt(0)); }
                int replacementOffset = offset - prefix.length();
                int replacementLength = prefix.length();
                if (null == proposals) {
                	proposals = new ArrayList<ICompletionProposal>();
                }
                for (CMakeCommand command : commands) {
                    final String name = isLowercase ? command.getName().toLowerCase() : command.getName();
                    final String desc = command.getDescription();
                    final String[] usages = command.getUsages();
                    
                    for (String usage : usages) {
                        final IContextInformation contextInfo = new ContextInformation(name, usage);
                        proposals.add(new CompletionProposal(name + EditorUtils.START_ARGS,
							                                replacementOffset, replacementLength,
							                                name.length() + 1, null, name + usage,
							                                contextInfo, desc));
                    }
                }
            }
        }
        else
        {
            if (null == proposals) {
            	proposals = new ArrayList<ICompletionProposal>();
            }
        	proposals = new ArrayList<ICompletionProposal>();
        	List<ICompletionProposal> propProposals = computePropertyCompletionProposals(viewer, offset);
        	if (propProposals != null)
        	{
            	proposals.addAll(propProposals);	
        	}
        	
        	List<ICompletionProposal> varProposals = computeVariableCompletionProposals(viewer, offset);
        	if (varProposals != null)
        	{
            	proposals.addAll(varProposals);	
        	}
        	
        	List<ICompletionProposal> resProposals = computeReservedWordCompletionProposals(viewer, offset);
        	if (resProposals != null)
        	{
            	proposals.addAll(resProposals);	
        	}
        	
        	List<ICompletionProposal> uvarProposals = computeUserVariableCompletionProposals(viewer, offset);
        	if (uvarProposals != null)
        	{
            	proposals.addAll(uvarProposals);	
        	}
        	
        }
        
        // Get the template Proposals from the super class
        
        return (null == proposals || proposals.size() == 0) ? null : proposals.toArray(new ICompletionProposal[proposals.size()]);
    }

    
    /**
     * Determines the portion of the word that has already been entered.
     * 
     * @param viewer  viewer being edited
     * @param offset  Offset into the document where content assist is desired.
     * @return Portion of the word that has already been entered.
     */
    public List<ICompletionProposal> computeVariableCompletionProposals( final ITextViewer viewer, 
    																     final int offset) {
		final IDocument doc = viewer.getDocument();
		List<ICompletionProposal> proposals = null;

		if (EditorUtils.inArguments(doc, offset)) {
			final String prefix = getPrefix(doc, offset);
			final List<CMakeVariable> variables = findPossibleVariables(prefix);

			if (!StringUtils.isBlank(prefix) && !variables.isEmpty()) {
				final boolean isLowercase = Character.isLowerCase(prefix.charAt(0));
				final int replacementOffset = offset - prefix.length();
				final int replacementLength = prefix.length();

				proposals = new ArrayList<ICompletionProposal>();
				for (CMakeVariable variable : variables) {
					final String name = isLowercase ? variable.getName().toUpperCase() : variable.getName();
					final String desc = variable.getDescription();
					final IContextInformation contextInfo = new ContextInformation(name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length(), null, name, contextInfo, desc));
				}
			}
		}

		return proposals;
	}

    /**
     * Determines the portion of the word that has already been entered.
     * 
     * @param viewer  viewer being edited
     * @param offset  Offset into the document where content assist is desired.
     * @return Portion of the word that has already been entered.
     */
    public List<ICompletionProposal> computeUserVariableCompletionProposals( final ITextViewer viewer, 
    																         final int offset) {
		final IDocument doc = viewer.getDocument();
		List<ICompletionProposal> proposals = null;

		if (EditorUtils.inArguments(doc, offset)) {
			final String prefix = getPrefix(doc, offset);
			final List<CMakeUserVariable> variables = findPossibleUserVariables(prefix, doc);

			if (!StringUtils.isBlank(prefix) && !variables.isEmpty()) {
				final int replacementOffset = offset - prefix.length();
				final int replacementLength = prefix.length();

				proposals = new ArrayList<ICompletionProposal>();
				for (CMakeUserVariable variable : variables) {
					final String name = variable.getName();
					final IContextInformation contextInfo = new ContextInformation(name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length(), null, name, contextInfo, null));
				}
			}
		}

		return proposals;
	}
    
    /**
     * Determine possible commands that start with the specified prefix.
     * 
     * @param prefix  Prefix for commands
     * @return Commands that begin with the specified prefix.
     */
    private List<CMakeUserVariable> findPossibleUserVariables( String prefix, IDocument doc)
    {
        final List<CMakeUserVariable> possibles = new ArrayList<CMakeUserVariable>();
        final CMakeUserVariables variables = CMakeUserVariableRule.userVariableMap.get(doc);
        Collection<CMakeUserVariable> commands = variables.getUserVariables();
        for (CMakeUserVariable command : commands) { 
                String name = command.getName();
                if (name.startsWith(prefix)) {
                    possibles.add(command);
                }
        }
        return possibles;
    }
    
    /**
     * Determine possible commands that start with the specified prefix.
     * 
     * @param prefix  Prefix for commands
     * @return Commands that begin with the specified prefix.
     */
    private List<CMakeVariable> findPossibleVariables(final String prefix)
    {
        final List<CMakeVariable> possibles = new ArrayList<CMakeVariable>();
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
     * 
     * @param viewer 
     * @param offset 
     * @return List of completion proposals.
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
     */
    public List<ICompletionProposal> computeReservedWordCompletionProposals(final ITextViewer viewer,
                                                            final int offset)
    {
        final IDocument doc = viewer.getDocument();
        List<ICompletionProposal> proposals = null;
        
        if (EditorUtils.inArguments(doc, offset)) {
            final String prefix = getPrefix(doc, offset);
            final List<CMakeReservedWord> reservedWords = findPossibleReservedWords(prefix);
            
            if (!StringUtils.isBlank(prefix) && !reservedWords.isEmpty()) {
                final boolean isLowercase = Character.isLowerCase(prefix.charAt(0));
                final int replacementOffset = offset - prefix.length();
                final int replacementLength = prefix.length();

                proposals = new ArrayList<ICompletionProposal>();
				for (CMakeReservedWord variable : reservedWords) {
					final String name = isLowercase ? variable.getName().toUpperCase() : variable.getName();
					final IContextInformation contextInfo = new ContextInformation(name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length() , null, name, contextInfo, name));
				}
            }
        }
        
        return proposals;
    }
    
    /**
     * Determine possible commands that start with the specified prefix.
     * 
     * @param prefix  Prefix for commands
     * @return Commands that begin with the specified prefix.
     */
    private List<CMakeReservedWord> findPossibleReservedWords(final String prefix)
    {
        final List<CMakeReservedWord> possibles =
            new ArrayList<CMakeReservedWord>();
        final Collection<CMakeReservedWord> commands = CMakeReservedWords.getCommands();
        final String lowerPrefix = prefix.toUpperCase();
        
        for (CMakeReservedWord command : commands) {
                final String name = command.getName();
                if (name.startsWith(lowerPrefix)) {
                    possibles.add(command);
                }
                else if (!possibles.isEmpty()) {
                    break;
                }
        }
        return possibles;
    }
    
/**
 * 
 * @param viewer
 * @param offset
 * @return Return list of property Completion proposals
 */
    public List<ICompletionProposal> computePropertyCompletionProposals(final ITextViewer viewer,
                                                            final int offset)
    {
        final IDocument doc = viewer.getDocument();
        List<ICompletionProposal> proposals = null;
        
        if (EditorUtils.inArguments(doc, offset)) {
            final String prefix = getPrefix(doc, offset);
            final List<CMakeProperty> properties = findPossibleProperties(prefix);
            
            if (!StringUtils.isBlank(prefix) && !properties.isEmpty()) {
                final boolean isLowercase = Character.isLowerCase(prefix.charAt(0));
                final int replacementOffset = offset - prefix.length();
                final int replacementLength = prefix.length();

                proposals = new ArrayList<ICompletionProposal>();
				for (CMakeProperty property : properties) {
					final String name = isLowercase ? property.getName().toUpperCase() : property.getName();
					final String desc = property.getDescription();
					final IContextInformation contextInfo = new ContextInformation(name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length(), null, name, contextInfo, desc));
				}
            }
        }
        
        return proposals;
    }
    
    /**
     * Determine possible commands that start with the specified prefix.
     * 
     * @param prefix  Prefix for commands
     * @return Commands that begin with the specified prefix.
     */
    private List<CMakeProperty> findPossibleProperties(final String prefix)
    {
        final List<CMakeProperty> possibles =
            new ArrayList<CMakeProperty>();
        final Collection<CMakeProperty> commands = CMakeProperties.getCommands();
        final String lowerPrefix = prefix.toUpperCase();
        
        for (CMakeProperty command : commands) {
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
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
     */
    public IContextInformation[] computeContextInformation(final ITextViewer viewer,
                                                           final int offset)
    {
        final IDocument doc = viewer.getDocument();
        final CMakeCommand cmd = EditorUtils.findContainingCommand(doc, offset);
        if (cmd != null) {
            final String[] usages = cmd.getUsages();
            final IContextInformation[] contexts = new IContextInformation[usages.length];
            
            int i = 0;
            for (String usage : usages) {
                contexts[i++] = new ContextInformation(usage, usage);
            }
            
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
                             (EditorUtils.findContainingCommand(this.doc, offset) != null);
                }
                catch (final BadLocationException e) {
                    CMakeEditorPlugin.logError(this, e);
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
                if (n == 0)
                {
                	return doc.get(n, offset - n);
                }
            }
        }
        catch (final BadLocationException e) {
            CMakeEditorPlugin.logError(this, e);
        }
        
        return "";      //$NON-NLS-1$
    }
    
    /**
     * 
     * @param prefix
     * @param templates
     * @return
     */
    private List<ICompletionProposal> findPossibleTemplates(String prefix, 
    														ICompletionProposal[] templates)
    {
        List<ICompletionProposal> possibles = new ArrayList<ICompletionProposal>();
        if (templates.length > 0)
        {
        	for (int i = 0; i < templates.length; i++) {
        		if (templates[i].getDisplayString().startsWith(prefix)) {
        			possibles.add(templates[i]);
        		}
			}
        }
       return (null == possibles || possibles.size() == 0) ? null : possibles;
    }
    
    
    
    /**
     * Determine possible commands that start with the specified prefix.
     * 
     * @param prefix  Prefix for commands
     * @return Commands that begin with the specified prefix.
     */
    private List<CMakeCommand> findPossibleCommands(final String prefix)
    {
        final List<CMakeCommand> possibles =
            new ArrayList<CMakeCommand>();
        final Collection<CMakeCommand> commands = CMakeCommands.getCommands();
        final String lowerPrefix = prefix.toLowerCase();
        
        for (CMakeCommand command : commands) {
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
                final CMakeCommand command = CMakeCommands.getCommand(word);
                if (command != null) {
                    description = command.getDescription();
                }
            }
            catch (final BadLocationException e) {
                CMakeEditorPlugin.logError(this, e);
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
            CMakeEditorPlugin.logError(this, e);
        }
        
        return region;
    }


}
