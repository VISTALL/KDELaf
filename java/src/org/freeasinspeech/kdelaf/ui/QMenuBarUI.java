package org.freeasinspeech.kdelaf.ui;
 
import java.util.Timer;
import java.util.TimerTask;
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
 * Class QMenuBarUI
 * MenuBarUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QMenuBarUI extends BasicMenuBarUI
{  
	/**
	 * Returns an instance of <code>QMenuBarUI</code>.
	 * @return an instance of <code>QMenuBarUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QMenuBarUI();
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
	 * Returns preferred size of JMenuBar.
	 * @param c menuBar for which to return preferred size
	 * @return Preferred size of the give menu bar.
	 */
	public Dimension getPreferredSize(JComponent c) {
		Dimension preff = null;
		try {
			preff = KdeLAF.getClient().getSimplePreferredSize(org.freeasinspeech.kdelaf.client.KDEConstants.SZ_MENUBAR);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
			preff = super.getPreferredSize(c);
		}
		return preff;
	}
	
	/**
	 * If the property <code>MenuBar.gradient</code> is set, then a gradient
	 * is painted as background, otherwise the normal superclass behaviour is
	 * called.
	 */
	public void update(Graphics g, JComponent c) {
		try {
			boolean horizontal = (c.getWidth() > c.getHeight());
			BufferedImage img = KdeLAF.getClient().getMenuBarImage(c.getWidth(), c.getHeight(), horizontal);
			g.drawImage(img, 0, 0, c);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}

}
