package org.freeasinspeech.kdelaf.ui;

import javax.swing.plaf.basic.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * Class QEditorPaneUI
 * EditorPaneUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QEditorPaneUI extends BasicEditorPaneUI
{	
	/**
	 * Returns an instance of <code>QEditorPaneUI</code>.
	 * @return an instance of <code>QEditorPaneUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QEditorPaneUI();
	}

	/**
	 * This method paints the Component.
	 *
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	public void update(Graphics g, JComponent c)
	{
		Utilities.configure(g);
		super.update(g, c);
	}
}
