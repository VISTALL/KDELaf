package org.freeasinspeech.kdelaf.ui;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;

/**
 * Class QRadioButtonMenuItemIcon
 * An icon displayed for RadioButtonMenuItemIcon components.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QRadioButtonMenuItemIcon implements Icon
{      
	/**
	 * Returns the width of the icon, in pixels.
	 * @return The width of the icon.
	 */
	public int getIconWidth() {
		int width = 15;
		try {
			width = KdeLAF.getClient().getPixelMetric(KDEConstants.JPM_MenuButtonIndicator);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
			width = 15;
		}
		return width;
	}
	
	/**
	 * Returns the height of the icon, in pixels.
	 * @return The height of the icon.
	 */
	public int getIconHeight() {
		int height = 15;
		try {
			height = KdeLAF.getClient().getPixelMetric(KDEConstants.JPM_MenuButtonIndicator);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
			height = 15;
		}
		return height;
	}
    
	/**
	 * Paints the icon.
	 * 
	 * @param c  the component.
	 * @param g  the graphics device.
	 * @param x  the x-coordinate.
	 * @param y  the y-coordinate.
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
	}
} 
