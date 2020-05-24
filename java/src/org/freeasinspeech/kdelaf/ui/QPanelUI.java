package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QPanelUI
 * PanelUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QPanelUI extends BasicPanelUI
{	
	/**
	 * Returns an instance of <code>QPanelUI</code>.
	 * @return an instance of <code>QPanelUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QPanelUI();
	}
}
