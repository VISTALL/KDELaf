package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
 
/**
 * Class QLabelUI
 * LabelUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QLabelUI extends BasicLabelUI {
	/**
	 * Returns an instance of <code>QTableUI</code>.
	 * @return an instance of <code>QTableUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QLabelUI();
	}

	/**
	 * This method paints the Component.
	 *
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	public void paint(Graphics g, JComponent c)
	{
		Utilities.configure(g);
		super.paint(g, c);
	}
}
