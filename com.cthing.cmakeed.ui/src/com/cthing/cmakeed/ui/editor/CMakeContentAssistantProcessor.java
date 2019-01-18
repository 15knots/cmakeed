/* *****************************************************************************
 * Copyright 2007 C Thing Software
 * All Rights Reserved.
 ******************************************************************************/

package com.cthing.cmakeed.ui.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationPresenter;
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
import com.cthing.cmakeed.ui.prefs.Preferences;

/**
 * Content assist for CMake commands.
 */
public class CMakeContentAssistantProcessor extends TemplateCompletionProcessor
											implements IContentAssistProcessor,
											           ITextHover
{
	/**
	 * @brief Validator class for constext information
	 */
	protected static class Validator implements IContextInformationValidator,
												IContextInformationPresenter {

		protected int fInstallOffset;
		protected boolean isCommand = false;
		protected ITextViewer viewer;

		/*
		 * @see IContextInformationValidator#isContextInformationValid(int)
		 */
		public boolean isContextInformationValid(int offset)
		{
			if (this.isCommand == false) {
				return false;
				}
			try {
	        	if (viewer.getDocument().getChar(offset - 1) != ')') {
	        		return true;
	            }
	        }
	        catch (final BadLocationException e) {
	            CMakeEditorPlugin.logError(EditorUtils.class, e);
	        }
	        catch (Exception e) {
	        	CMakeEditorPlugin.logError(null, e);
	        }
			return false;
		}

		/*
		 * @see IContextInformationValidator#install(IContextInformation, ITextViewer, int)
		 */
		public void install(IContextInformation info, ITextViewer viewer, int offset) {
			fInstallOffset= offset;
			if (info.getContextDisplayString().startsWith("Command::") == true)
			{
				this.isCommand = true;
			}
			this.viewer = viewer;
		}

		/*
		 * @see org.eclipse.jface.text.contentassist.IContextInformationPresenter#updatePresentation(int, TextPresentation)
		 */
		public boolean updatePresentation(int documentPosition, TextPresentation presentation) {
			return false;
		}

	}

	// protected IContextInformationValidator fValidator= new Validator();

    private final CMakeNameDetector detector = new CMakeNameDetector();
    private static final String TEMPLATE_IMAGE= "$nl$/icons/CMakeTemplate.png"; //$NON-NLS-1$
	private static final String COMMAND_IMAGE = "$nl$/icons/CMakeCommand.png"; //$NON-NLS-1$
	private static final String PROPERTY_IMAGE = "$nl$/icons/CMakeProperty.png"; //$NON-NLS-1$
	private static final String VARIABLE_IMAGE = "$nl$/icons/CMakeVariable.png"; //$NON-NLS-1$
	private static final String RESERVED_WORD_IMAGE ="$nl$/icons/CMakeReservedWord.png"; //$NON-NLS-1$


    /**
     * Default constructor for the class.
     */
    public CMakeContentAssistantProcessor()
    {
    //	 System.out.println("CMakeContentAssistantProcessor Constructor");
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
		Image image= registry.get(TEMPLATE_IMAGE);
		if (image == null) {
			ImageDescriptor desc= CMakeEditorUI.imageDescriptorFromPlugin("com.cthing.cmakeed.ui", TEMPLATE_IMAGE); //$NON-NLS-1$
			registry.put(TEMPLATE_IMAGE, desc);
			image= registry.get(TEMPLATE_IMAGE);
		}
		return image;
	}

	/**
	 *
	 * @param imageType
	 */
	protected Image getCustomImage(final String imageType)
	{
		ImageRegistry registry= CMakeEditorUI.getDefault().getImageRegistry();
		Image image= registry.get(imageType);
		if (image == null) {
			ImageDescriptor desc= CMakeEditorUI.imageDescriptorFromPlugin("com.cthing.cmakeed.ui", imageType); //$NON-NLS-1$
			registry.put(imageType, desc);
			image= registry.get(imageType);
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
        if (null == proposals) {
        	proposals = new ArrayList<ICompletionProposal>();
        }

        if (!EditorUtils.inArguments(doc, offset)) {
        //-- Get any command proposals
            final List<CMakeCommand> commands = findPossibleCommands(prefix);

            if ( !commands.isEmpty()) {
                boolean isUpperCase = CMakeEditorPlugin.getDefault().getPreferenceStore().getBoolean(Preferences.UPPER_CASE_COMMANDS);
                if (prefix.length() > 0) { Character.isLowerCase(prefix.charAt(0)); }
                int replacementOffset = offset - prefix.length();
                int replacementLength = prefix.length();
                for (CMakeCommand command : commands) {
                    final String name = isUpperCase ? command.getName().toUpperCase() : command.getName().toLowerCase();
                    final String desc = command.getDescription();
                    final String[] usages = command.getUsages();

                    for (String usage : usages) {
                        final IContextInformation contextInfo
                              = new ContextInformation( "Command::" + name, usage);
                        String replacementString = name + "(";
                        int    cursorPosition = name.length() + 1;
                        Image image = getCustomImage(COMMAND_IMAGE);
                        String displayString = name + usage;
                        String additionalProposalInfo = desc;
                        proposals.add(new CompletionProposal(replacementString,
							                                replacementOffset, replacementLength,
							                                cursorPosition, image, displayString ,
							                                contextInfo, additionalProposalInfo));

                    }
                }
            }
        }
        else
        {
//            if (null == proposals) {
//            	proposals = new ArrayList<ICompletionProposal>();
//            }
        	proposals = new ArrayList<ICompletionProposal>();
        	List<ICompletionProposal> propProposals = computePropertyCompletionProposals(viewer, offset);
        	if (propProposals != null)
        	{
            	proposals.addAll(propProposals);
        	}

        	List<ICompletionProposal> varProposals = computeCMakeVariableCompletionProposals(viewer, offset);
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

        return (proposals.size() == 0) ? null : proposals.toArray(new ICompletionProposal[proposals.size()]);
    }


    /**
     * Determines the portion of the word that has already been entered.
     *
     * @param viewer  viewer being edited
     * @param offset  Offset into the document where content assist is desired.
     * @return Portion of the word that has already been entered.
     */
    public List<ICompletionProposal> computeCMakeVariableCompletionProposals( final ITextViewer viewer,
    																     final int offset) {
		final IDocument doc = viewer.getDocument();
		List<ICompletionProposal> proposals = null;

		if (EditorUtils.inArguments(doc, offset)) {
			final String prefix = getPrefix(doc, offset);
			final List<CMakeVariable> variables = findPossibleCMakeVariables(prefix);

			if (!StringUtils.isBlank(prefix) && !variables.isEmpty()) {
				final boolean isLowercase = Character.isLowerCase(prefix.charAt(0));
				final int replacementOffset = offset - prefix.length();
				final int replacementLength = prefix.length();

				proposals = new ArrayList<ICompletionProposal>();
				for (CMakeVariable variable : variables) {
					final String name = isLowercase ? variable.getName().toUpperCase() : variable.getName();
					final String desc = variable.getDescription();
					final IContextInformation contextInfo = new ContextInformation("CMakeVariable::" + name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length(), getCustomImage(VARIABLE_IMAGE), name, contextInfo, desc));
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
    private List<CMakeVariable> findPossibleCMakeVariables(final String prefix)
    {
        final List<CMakeVariable> possibles = new ArrayList<CMakeVariable>();
        final Collection<CMakeVariable> cmakeVariables = CMakeVariables.getCommands();
        final String upperPrefix = prefix.toUpperCase();

        /* VERY important that the list is kept in alphabetical order otherwise
         * this little block of code will only pick up the first match and not
         * all possible matches
         */
        for (CMakeVariable cmakeVariable : cmakeVariables) {
            final String name = cmakeVariable.getName();
            if (name.startsWith(upperPrefix)) {
                possibles.add(cmakeVariable);
            }
            else if (!possibles.isEmpty()) {
                break;
            }
        }

        return possibles;
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
					final IContextInformation contextInfo = new ContextInformation("CMakeUserVariable::" + name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length(), getCustomImage(VARIABLE_IMAGE), name, contextInfo, null));
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
        final CMakeUserVariables documentUserVariables = CMakeUserVariableRule.userVariableMap.get(doc);
        Collection<CMakeUserVariable> userVariables = documentUserVariables.getUserVariables();
        if (prefix.length() == 0)
        {
            possibles.addAll(userVariables);
            return possibles;
        }
        for (CMakeUserVariable userVariable : userVariables) {
                String name = userVariable.getName();
                if (name.startsWith(prefix)) {
                    possibles.add(userVariable);
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
					final IContextInformation contextInfo = new ContextInformation("ReservedWord::" + name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length() , getCustomImage(RESERVED_WORD_IMAGE), name, contextInfo, name));
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
					final IContextInformation contextInfo = new ContextInformation("CMakeProperty::" + name, name);
					proposals.add(new CompletionProposal(name ,
							replacementOffset, replacementLength,
							name.length(), getCustomImage(PROPERTY_IMAGE), name, contextInfo, desc));
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
    	/*
        final IDocument doc = viewer.getDocument();
//        final CMakeCommand cmd = EditorUtils.findContainingCommand(doc, offset);
        CMakeCommand cmd = CMakeCommands.getCommand("if");
        if (cmd != null) {
            final String[] usages = cmd.getUsages();
            final IContextInformation[] contexts = new IContextInformation[usages.length];

            int i = 0;
            for (String usage : usages) {
                contexts[i++] = new ContextInformation(cmd.getName(), usage);
            }

            return contexts;
        }
        */
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
     */
	public IContextInformationValidator getContextInformationValidator() {
		return new Validator();
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
        return new char[] { '(', };
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
     */
  private List<ICompletionProposal> findPossibleTemplates(String prefix, ICompletionProposal[] templates)    {
        List<ICompletionProposal> possibles = new ArrayList<>();
        if (templates.length > 0)
        {
        	for (int i = 0; i < templates.length; i++) {
        		if (templates[i].getDisplayString().startsWith(prefix)) {
        			possibles.add(templates[i]);
        		}
			}
        }
       return (possibles.size() == 0) ? null : possibles;
    }



    /**
     * Determine possible commands that start with the specified prefix.
     *
     * @param prefix  Prefix for commands
     * @return Commands that begin with the specified prefix.
     */
    private List<CMakeCommand> findPossibleCommands(final String prefix)
    {
        final List<CMakeCommand> possibles = new ArrayList<CMakeCommand>();
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
