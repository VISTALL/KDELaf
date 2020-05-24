package org.freeasinspeech.kdelaf.ui;
 
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import org.freeasinspeech.kdelaf.*;

/**
 * Class QMenuItemUI
 * MenuItemUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QMenuItemUI extends BasicMenuItemUI
{
	/** Stores the roolover listeners. */
	protected static Hashtable ItemListeners = new Hashtable();
		
	/**
	 * Returns an instance of <code>QMenuItemUI</code>.
	 * @return an instance of <code>QMenuItemUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QMenuItemUI();
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
		Utilities.registerRollover((AbstractButton)c);
		Utilities.registerPressed((AbstractButton)c);
		/*
		((AbstractButton)c).setRolloverEnabled(true);
		ItemListener ItemListener = new ItemListener(((AbstractButton)c).getModel());
		ItemListeners.put(c, ItemListener);
		((AbstractButton)c).addMouseListener(ItemListener);*/
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
		Utilities.unregisterRollover((AbstractButton)c);
		Utilities.unregisterPressed((AbstractButton)c);
		/*
		if (ItemListeners.containsKey(c)) {
			ItemListener ItemListener = (ItemListener)ItemListeners.get(c);
			if (ItemListener != null)
				((AbstractButton)c).removeMouseListener(ItemListener);
		}*/
	}

	/**
	* Calculate the preferred size of this component
	* @param c The component to measure
	* @return The preferred dimensions of the component
	*/
	public Dimension getPreferredSize(JComponent c) {
		KdeLAF.declareRoot(c);
		// [!]HACK
		if (Utilities.isClasspath()) {
			if (!Utilities.isRolloverRegistered((AbstractButton)c))
				Utilities.registerRollover((AbstractButton)c);
			if (!Utilities.isPressedRegistered((AbstractButton)c))
				Utilities.unregisterPressed((AbstractButton)c);
		}
		return super.getPreferredSize(c);
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
	 * 
	 * @param g the paint graphics
	 * @param menuItem menu item to be painted
	 * @param bgColor selection background color
	 * @since 1.4
	 */
	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		// [!]HACK
		if (Utilities.isClasspath()) {
			if (!Utilities.isRolloverRegistered(menuItem))
				Utilities.registerRollover(menuItem);
			if (!Utilities.isPressedRegistered(menuItem))
				Utilities.unregisterPressed(menuItem);
		}
		try {
			BufferedImage img = KdeLAF.getClient().getMenuItemImage(menuItem);
			g.drawImage(img, 0, 0, menuItem);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}
	
	
	/**
	 * Class ItemListener
	 * Listener for the and the clicked roolover effect.
	 * 
	 * @author sekou.diakite@fais.org
	 */
	protected class ItemListener extends MouseAdapter
	{
		/** The button model. */
		protected ButtonModel model;
		/**
		 * Constructor
		 * @param model The button model
		 */
		public ItemListener(ButtonModel model) {
			this.model = model;
			if (model == null)
				org.freeasinspeech.kdelaf.Logger.log("GNU CLASSPATH BUG, getModel() == null");
		}
		/**
		 * Activate the pressed effect
		 */
		public void mousePressed(MouseEvent e) {
			// [!] classpath has model bug, getModel() == null
			if (model != null)
				model.setPressed(true);
		}
		/**
		 * Deactivate the pressed effect
		 */
		public void mouseReleased(MouseEvent e) {
			// [!] classpath has model bug, getModel() == null
			if (model != null) {
				model.setPressed(false);
				model.setRollover(false);
			}
		}
		/**
		 * Activate the rollover
		 */
		public void mouseEntered(MouseEvent e) {
			// [!] classpath has model bug, getModel() == null
			if (model != null)
				model.setRollover(true);
		}
		/**
		 * Deactivate the rollover
		 */
		public void mouseExited(MouseEvent e) {
			// [!] classpath has model bug, getModel() == null
			if (model != null)
				model.setRollover(false);
		}
	}
}
