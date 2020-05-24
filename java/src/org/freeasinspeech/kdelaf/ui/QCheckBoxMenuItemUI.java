package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import java.awt.event.*;

import org.freeasinspeech.kdelaf.*;
 
/**
 * Class QCheckBoxMenuItemUI
 * CheckBoxMenuItemUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QCheckBoxMenuItemUI extends QMenuItemUI implements MouseListener {
	/** True when the rollover is active. */
	protected boolean rollover = false;
		
	/**
	 * Returns an instance of <code>QCheckBoxMenuItemUI</code>.
	 * @return an instance of <code>QCheckBoxMenuItemUI</code>.
	 */
	public static ComponentUI createUI(JComponent c)  {
		return new QCheckBoxMenuItemUI();
	}
	
	/**
	 * Returns the prefix for entries in the {@link UIDefaults} table.
	 * @return "CheckBoxMenuItem"
	 */
	public String getPropertyPrefix() {
		return "CheckBoxMenuItem";
	}
	
	/**
	 * Calculate the preferred size of this component
	 * @param c The component to measure
	 * @return The preferred dimensions of the component
	 */
	public Dimension getPreferredSize(JComponent c) {
		return (new JMenuItem(((JMenuItem)c).getText())).getPreferredSize();
	}
	
  /**
   * Install the QMenuItemUI as the UI for a particular component.
   * This means registering all the UI's listeners with the component,
   * and setting any properties of the button which are particular to 
   * this look and feel.
   *
   * @param c The component to install the UI into
   */
	public void installUI(JComponent c) {
		super.installUI(c);
		((AbstractButton)c).addMouseListener(this);
	}
	
	/**
	 * Puts the specified component into the state it had before
	 * {@link #installUI} was called.
	 *
	 * @param c the component for which this delegate has provided
	 *        services.
	 *
	 * @see #installUI
	 * @see javax.swing.JComponent#setUI
	 * @see javax.swing.JComponent#updateUI
	 */
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		((AbstractButton)c).removeMouseListener(this);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param item DOCUMENT ME!
	 * @param e DOCUMENT ME!
	 * @param path DOCUMENT ME!
	 * @param manager DOCUMENT ME!
	 */
	public void processMouseEvent(JMenuItem item, MouseEvent e, MenuElement[] path, MenuSelectionManager manager) {
		item.processMouseEvent(e, path, manager);
	}
	
	/**
	 * Draws the background of the menu item.
	 * 
	 * @param g the paint graphics
	 * @param menuItem menu item to be painted
	 * @param bgColor selection background color
	 * @since 1.4
	 */
	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		try {
			BufferedImage img = KdeLAF.getClient().getCheckBoxMenuItemImage(menuItem, rollover);
			g.drawImage(img, 0, 0, menuItem);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}
	
	//////////////////////// MOUSE EVENTS
	/**
	 * Activate the pressed effect
	 */
	public void mouseClicked(MouseEvent e) {
	}
	/**
	 * Activate the pressed effect
	 */
	public void mousePressed(MouseEvent e) {
	}
	/**
	 * Deactivate the pressed effect
	 */
	public void mouseReleased(MouseEvent e) {
		rollover = false;
	}
	/**
	 * Activate the rollover
	 */
	public void mouseEntered(MouseEvent e) {
		rollover = true;
	}
	/**
	 * Deactivate the rollover
	 */
	public void mouseExited(MouseEvent e) {
		rollover = false;
	}
}


