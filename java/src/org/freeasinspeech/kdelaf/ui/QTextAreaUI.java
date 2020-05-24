package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QTextAreaUI
 * TextAreaUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QTextAreaUI extends BasicTextAreaUI
{	
	/**
	 * Returns an instance of <code>QTextAreaUI</code>.
	 * @return an instance of <code>QTextAreaUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QTextAreaUI();
	}
}
