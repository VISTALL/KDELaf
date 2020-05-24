package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QRootPaneUI
 * RootPaneUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QRootPaneUI extends BasicRootPaneUI
{	
	/**
	 * Returns an instance of <code>QRootPaneUI</code>.
	 * @return an instance of <code>QRootPaneUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QRootPaneUI();
	}
}
