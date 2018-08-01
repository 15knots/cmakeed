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

package com.cthing.cmakeed.ui.editor.rules;

import java.util.Arrays;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

import com.cthing.cmakeed.ui.editor.CMakePartitionScanner;

/**
 * Matches a multi-line bracket comment.
 *
 * @author Martin Weber
 */
public class BracketCommentRule extends MultiLineRule {

  /**
   */
  public BracketCommentRule() {
    super("egal", "egal", new Token(CMakePartitionScanner.COMMENT_CONTENT_TYPE), (char) 0, true);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.eclipse.jface.text.rules.PatternRule#doEvaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
   */
  @Override
  protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
    if (resume) {
      return super.doEvaluate(scanner, resume);
    }
    // evaluate start sequence '#[['...
    int c = scanner.read();
    if (c == '#') {
      c = scanner.read();
      if (c == '[') {
        int len = evaluateLength(scanner);
        if (len >= 0) {
          // bracket detected, construct the corresponding sequences and let super do the job
          char[] seq = new char[len + 3];
          Arrays.fill(seq, 2, len + 2, '=');
          seq[0] = '#';
          seq[1] = '[';
          seq[len + 2] = '[';
          super.fStartSequence = seq;
          super.fEndSequence = new String(seq, 1, seq.length - 1).replace('[', ']').toCharArray();
          // rewind scanner
          unread(scanner, len + 3);
          // let super do the job
          return super.doEvaluate(scanner, resume);
        }
      }
      scanner.unread();
    }

    scanner.unread();
    return Token.UNDEFINED;
  }

  /**
   * Evaluates the bracket length.
   *
   * @return the length of the bracket (may be zero), {@code -1} if no match is found.
   */
  private int evaluateLength(ICharacterScanner scanner) {
    int len = 0;
    int reads = 0;

    for (;; reads++) {
      switch (scanner.read()) {
      case '=':
        len++;
        break;
      case '[':
        return len;
      default:
        unread(scanner, reads + 1);
        return -1; // no match
      }
    }
  }

  /**
   * Unread a count elements of characters.
   *
   * @param scanner
   *          Scanner to unread
   * @param count
   *          number of characters to unread
   */
  private static void unread(final ICharacterScanner scanner, int count) {
    while (count-- > 0) {
      scanner.unread();
    }
  }

}
