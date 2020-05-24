package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import java.util.*;

import org.freeasinspeech.kdelaf.*;
 
/**
 * Class KToggleButtonUI
 * ToggleButtonUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class KToggleButtonUI extends KButtonUI {
	
	/** Stores the unique instance. */
	protected static KToggleButtonUI toggleButtonUI = new KToggleButtonUI();
  
	/**
	 * Returns an instance of <code>KToggleButtonUI</code>.
	 * @return an instance of <code>KToggleButtonUI</code>.
	 */
	public static ComponentUI createUI(JComponent b) {
		return toggleButtonUI;
	}

	/**
	 * Paints the button background
	 * @param g The graphic device
	 * @param c The component
	 */
	protected void paintBackground(Graphics g, AbstractButton b) {
		if (b.isOpaque()) {
			JToggleButton.ToggleButtonModel model = (JToggleButton.ToggleButtonModel)b.getModel();
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
				if (b.getParent() instanceof JToolBar) {
					boolean horizontal = (((JToolBar)b.getParent()).getOrientation() == SwingConstants.HORIZONTAL);
					Dimension dim =  b.getParent().getPreferredSize();
					int tbHorW = horizontal ? dim.height : dim.width;			
					img = KdeLAF.getClient().getToolbarButtonImage(width, height, tbHorW, enabled, pressed || selected, rollover, focus, contentAreaFilled, borderPainted, horizontal);
				}
				else
					img = KdeLAF.getClient().getButtonImage(width, height, enabled, pressed || selected, rollover, focus, contentAreaFilled, borderPainted);
				g.drawImage(img, 0, 0, b);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
			}
		}
	}
}

