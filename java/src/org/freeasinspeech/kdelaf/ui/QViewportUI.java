package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QViewportUI
 * ViewportUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QViewportUI extends BasicViewportUI
{	
	/**
	 * Returns an instance of <code>QViewportUI</code>.
	 * @return an instance of <code>QViewportUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QViewportUI();
	}
}
