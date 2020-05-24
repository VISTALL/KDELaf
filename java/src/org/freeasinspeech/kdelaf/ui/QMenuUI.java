package org.freeasinspeech.kdelaf.ui;
 
import java.util.Hashtable;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import org.freeasinspeech.kdelaf.*;

/**
 * Class QMenuUI
 * MenuUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QMenuUI extends BasicMenuUI
{
	/** Stores the roolover listeners. */
	protected static Hashtable MenuListeners = new Hashtable();
		
	/**
	 * Returns an instance of <code>QMenuUI</code>.
	 * @return an instance of <code>QMenuUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QMenuUI();
	}

	/**
	 * This method paints the Component.
	 *
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	public void paint(Graphics g, JComponent c) {
		Utilities.configure(g);
		super.paint(g, c);
	}
	  
	/**
	 * Draws the background of the menu item.
	 * @param g the paint graphics
	 * @param menuItem menu item to be painted
	 * @param bgColor selection background color
	 */
	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		try {
			boolean topLevel = ( (menuItem instanceof JMenu) && (((JMenu)menuItem).isTopLevelMenu()) );
			BufferedImage img = KdeLAF.getClient().getMenuImage(menuItem.getWidth(), menuItem.getHeight(), topLevel, menuItem.isEnabled(), menuItem.isSelected());
			g.drawImage(img, 0, 0, menuItem);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}
}
