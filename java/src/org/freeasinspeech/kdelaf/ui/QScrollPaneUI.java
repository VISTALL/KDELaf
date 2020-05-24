package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QScrollPaneUI
 * ScrollPaneUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QScrollPaneUI extends BasicScrollPaneUI
{	
	/**
	 * Returns an instance of <code>QScrollPaneUI</code>.
	 * @return an instance of <code>QScrollPaneUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QScrollPaneUI();
	}
}
