package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
/*
		ir.moveBy(pixelMetric(PM_ButtonShiftHorizontal, widget),
			  pixelMetric(PM_ButtonShiftVertical, widget));
	    }

	    if (button->isMenuButton()) {
		int mbi = pixelMetric(PM_MenuButtonIndicator, widget);
		*/
 
/**
 * Class KButtonUI
 * ButtonUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KButtonUI extends BasicButtonUI implements PropertyChangeListener {
	/** The unique instance.*/
	static KButtonUI b = new KButtonUI();
	/** The horizontal shift on click. */
	protected static int shiftHorizontal = -5000;
	/** The vertical shift on click. */
	protected static int shiftVertical = -5000;

	/**
	 * Returns an instance of <code>KButtonUI</code>.
	 * @return an instance of <code>KButtonUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return b;
	}

	/**
	 * Install the KButtonUI as the UI for a particular component.
	 * This means registering all the UI's listeners with the component,
	 * and setting any properties of the button which are particular to 
	 * this look and feel.
	 *
	 * @param c The component to install the UI into
	 */
	public void installUI(JComponent c) {
		super.installUI(c);
		Utilities.registerRollover((AbstractButton)c);
		((AbstractButton)c).addPropertyChangeListener(this);
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
		((AbstractButton)c).removePropertyChangeListener(this);
		Utilities.unregisterRollover((AbstractButton)c);
	}

	/**
	 * Calculate the minimum size of this component
	 * @param c The component to measure
	 * @return The minimum dimensions of the component
	 */
	public Dimension getMinimumSize(JComponent c) {
		return getPreferredSize(c);
	}	

	/**
	 * Calculate the preferred size of this component
	 * @param c The component to measure
	 * @return The preferred dimensions of the component
	 */
	public Dimension getPreferredSize(JComponent c) {
		KdeLAF.declareRoot(c);
		// [!]HACK
		if (Utilities.isClasspath() && !Utilities.isRolloverRegistered((AbstractButton)c))
			Utilities.registerRollover((AbstractButton)c);
		
		Dimension dPreff = null;
		Dimension dParentPreff = super.getPreferredSize(c);
		if (c instanceof QToolBarUI.QToolBarHandle)
			dPreff = ((QToolBarUI.QToolBarHandle)c).getMinimumSize();
		else {
			AbstractButton b = (AbstractButton)c;
			String text = b.getText();
			Icon icon = b.getIcon();
			if (text == null)
				text = "";
			int width = (icon == null) ? 0 : icon.getIconWidth();
			int height = (icon == null) ? 0 : icon.getIconHeight();
			try {
				if ( (b instanceof KToolButton) || (b.getParent() instanceof JToolBar) )
					dPreff = KdeLAF.getClient().getToolbarButtonPreferredSize(text, width, height);
				else
					dPreff = KdeLAF.getClient().getButtonPreferredSize(text, width, height);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				dPreff = dParentPreff;
			}
		}
		dPreff.width = Math.max(dPreff.width, dParentPreff.width);
		dPreff.height = Math.max(dPreff.height, dParentPreff.height);
		return dPreff;
	}
	
	/**
	 * Apply the horizontal shift on click and the The vertical shift on click.
	 * @param r The rectangle to apply the sifts  to.
	 * @return r with the shifts applied.
	 */
	protected Rectangle applyShifts(Rectangle r) {
		// Gets the static value if necessary
		if ( (shiftHorizontal == -5000) && (shiftVertical == -5000)  ) {
			try {
				shiftHorizontal = KdeLAF.getClient().getPixelMetric(KDEConstants.JPM_ButtonShiftHorizontal);
				shiftVertical = KdeLAF.getClient().getPixelMetric(KDEConstants.JPM_ButtonShiftVertical);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				shiftHorizontal = 2;
				shiftVertical = 2;
			}
		}
		r.x += shiftHorizontal;
		r.y += shiftVertical;
		return r;
	}
	
	/**
	 * Returns true if the shifts must be applied.
	 * @return true if the shifts must be applied.
	 */
	protected boolean mustApplyShifts(AbstractButton b) {
		return b.getModel().isPressed();
	}

	/**
	 * Paints the component
	 * @param g The graphic device
	 * @param c The component
	 */
	public void paint(Graphics g, JComponent c) {
		Utilities.configure(g);
		// [!]HACK
		if (Utilities.isClasspath() && !Utilities.isRolloverRegistered((AbstractButton)c))
			Utilities.registerRollover((AbstractButton)c);
		paintBackground(g, (AbstractButton)c);
		super.paint(g, c);
	}

	/**
	 * Paints the button pressed background
	 * @param g The graphic device
	 * @param c The component
	 */
	protected void paintButtonPressed(Graphics g, AbstractButton b) {
	}

	/**
	 * Paints the button focus indicator
	 * @param g The graphic device
	 * @param c The component
	 */
	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
	}

	/**
	* Paints the button background
	* @param g The graphic device
	* @param c The component
	*/
	protected void paintBackground(Graphics g, AbstractButton b) {
		if (b.isOpaque()) {
			ButtonModel model = b.getModel();
			int width = b.getWidth();
			int height = b.getHeight();
			boolean enabled = model.isEnabled();
			boolean pressed = model.isPressed();
			boolean rollover = model.isRollover();
			boolean focus = (b.hasFocus() && b.isFocusPainted());
			boolean contentAreaFilled = b.isContentAreaFilled();
			boolean borderPainted = b.isBorderPainted();
			boolean selected = model.isSelected();
			try {
				BufferedImage img = null;
				if ( (b instanceof KToolButton) || (b.getParent() instanceof JToolBar) ) {
					boolean horizontal = (((JToolBar)b.getParent()).getOrientation() == SwingConstants.HORIZONTAL);
					Dimension dim =  b.getParent().getPreferredSize();
					int tbHorW = horizontal ? dim.height : dim.width;			
					img = KdeLAF.getClient().getToolbarButtonImage(width, height, tbHorW, enabled, pressed, rollover, focus, contentAreaFilled, borderPainted, horizontal);
				}
				else
					img = KdeLAF.getClient().getButtonImage(width, height, enabled, pressed, rollover, focus, contentAreaFilled, borderPainted);
				g.drawImage(img, 0, 0, b);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
			}
		}
	}

	/**
	 * Paints the "text" property of an {@link AbstractButton}.
	 * @param g The graphics context to paint with
	 * @param b The button to paint the state of
	 * @param textRect The area in which to paint the text
	 * @param text The text to paint
	 * @since 1.4
	 */
	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
		super.paintText(g, b, mustApplyShifts(b) ? applyShifts(textRect) : textRect, text);
	}
	
	/**
	 * Paint the icon for this component. Depending on the state of the component and the availability of the button's various icon properties,
	 * this might mean painting one of several different icons.
	 * @param g Graphics context to paint with
	 * @param c Component to paint the icon of
	 * @param iconRect Rectangle in which the icon should be painted
	 */
	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		super.paintIcon(g, c, mustApplyShifts((AbstractButton)c) ? applyShifts(iconRect) : iconRect);
	}
	
	// ******* EVENTS ********
	public void propertyChange(PropertyChangeEvent e) {
		// Force the button to have at least the minimum size.
		if (e.getPropertyName().equals("preferredSize")) {
			Object src = e.getSource();
			if (src instanceof AbstractButton) {
				AbstractButton b = (AbstractButton)src;
				Dimension newP = (Dimension)e.getNewValue();
				Dimension min = getMinimumSize(b);
				newP.height = Math.max(newP.height, min.height);
				newP.width = Math.max(newP.width, min.width);
			}
		}
	}
}
