package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QTextPaneUI
 * TextPaneUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QTextPaneUI extends BasicTextPaneUI
{	
	/**
	 * Returns an instance of <code>QTextPaneUI</code>.
	 * @return an instance of <code>QTextPaneUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QTextPaneUI();
	}
}
