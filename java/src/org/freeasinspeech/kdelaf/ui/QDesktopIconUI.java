package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;


import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
/**
 * Class QDesktopIconUI
 * DesktopIconUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QDesktopIconUI extends BasicDesktopIconUI {
	
	/**
	 * Constructor.
	 */
	public QDesktopIconUI() {
		super();
	}
	
	/**
	 * Returns an instance of <code>QDesktopIconUI</code>.
	 * @return an instance of <code>QDesktopIconUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QDesktopIconUI();
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