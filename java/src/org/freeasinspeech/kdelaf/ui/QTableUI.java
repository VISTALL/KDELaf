package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QTableUI
 * TableUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QTableUI extends BasicTableUI
{	
	/**
	 * Returns an instance of <code>QTableUI</code>.
	 * @return an instance of <code>QTableUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QTableUI();
	}
}
